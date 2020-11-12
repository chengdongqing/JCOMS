package top.chengdongqing.common.payment.wxpay.v3;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.encrypt.EncryptAlgorithm;
import top.chengdongqing.common.encrypt.Encryptor;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IPayment;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.entity.PayResEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.WxStatus;
import top.chengdongqing.common.payment.wxpay.v3.callback.ResourceHolder;
import top.chengdongqing.common.payment.wxpay.v3.callback.WxCallback;
import top.chengdongqing.common.payment.wxpay.v3.reqpay.ReqPayContext;
import top.chengdongqing.common.transformer.StrToBytes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 微信支付
 * V3
 *
 * @author Luyao
 */
@Slf4j
@Component
public class WxV3Payment implements IPayment {

    @Autowired
    private WxConstants constants;
    @Autowired
    private WxV3Constants v3constants;
    @Autowired
    private WxV3Helper helper;

    @Override
    public Ret requestPayment(PayReqEntity entity, TradeType tradeType) {
        return new ReqPayContext(tradeType).request(entity);
    }

    @Override
    public Ret handleCallback(Map<String, String> params) {
        if (params.isEmpty()) return toFailJson("参数错误");
        log.info("微信支付回调V3参数: {}", params);
        // 将弱类型MAP转强类型对象
        WxCallback wxCallback = WxCallback.of(params);

        // 验证签名
        boolean verify = WxV3Signer.verify(wxCallback, v3constants.getPublicKey());
        if (!verify) return toFailJson("验签失败");

        // 获取请求体中的加密数据
        String resourceHolderJson = JSON.parseObject(wxCallback.getBody()).getString("resource");
        ResourceHolder resourceHolder = JSON.parseObject(resourceHolderJson, ResourceHolder.class);
        // 获取密文、随机数、关联数据
        byte[] ciphertext = StrToBytes.of(resourceHolder.getCiphertext()).fromBase64();
        byte[] iv = resourceHolder.getNonce().getBytes();
        String associatedData = resourceHolder.getAssociatedData();
        // 解密数据
        String resourceJson = Encryptor.decrypt(EncryptAlgorithm.AES_GCM_NoPadding,
                ByteUtils.concatenate(iv, ciphertext),
                constants.getMchId(), associatedData).toText();
        log.info("微信支付回调数据解密后：{}", resourceJson);
        ResourceHolder.Resource resource = JSON.parseObject(resourceJson, ResourceHolder.Resource.class);

        // 判断支付结果
        if (!resource.isTradeSuccess()) return toFailJson("交易失败");

        // 封装支付信息
        PayResEntity payDetails = PayResEntity.builder()
                .orderNo(resource.getOutTradeNo())
                .paymentNo(resource.getTransactionId())
                // 将单位从分转为元
                .paymentAmount(new BigDecimal(resource.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN))
                // 转换支付时间
                .paymentTime(LocalDateTime.parse(resource.getSuccessTime(), DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        // 返回回调结果
        return Ret.ok(CallbackResponseEntity.builder()
                .json(Kv.go("code", WxStatus.SUCCESS).toJson())
                .details(payDetails)
                .build()
        );
    }

    /**
     * 获取回调处理失败响应给微信服务器的JSON
     *
     * @param errorMsg 错误信息
     * @return 带JSON的响应对象
     */
    private Ret toFailJson(String errorMsg) {
        return Ret.fail(Kv
                .go("code", WxStatus.FAIL)
                .add("message", errorMsg)
                .toJson());
    }

    @Override
    public Ret requestClose(String orderNo) {
        // 获取请求头
        String apiPath = WxV3Helper.getTradeApi(v3constants.getCloseUrl().formatted(orderNo));
        String body = Kv.go("mchid", constants.getMchId()).toJson();
        Kv<String, String> headers = helper.getAuthorization(HttpMethod.POST, apiPath, body);

        // 发送请求
        String requestUrl = helper.getRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求关闭订单：{}，\n请求头：{}，\n请求体：{}，响应结果：{}",
                requestUrl,
                headers,
                body,
                response);
        return response.statusCode() == 204 ? Ret.ok() : Ret.fail();
    }

    @Override
    public Ret requestRefund(String orderNo, String refundNo, BigDecimal totalAmount, BigDecimal refundAmount) {
        return null;
    }

    /**
     * 支付回调响应对象
     */
    @Data
    @Builder
    public static class CallbackResponseEntity {
        /**
         * 返回给微信服务器的json
         */
        private String json;
        /**
         * 收集的支付详情
         */
        private PayResEntity details;
    }
}
