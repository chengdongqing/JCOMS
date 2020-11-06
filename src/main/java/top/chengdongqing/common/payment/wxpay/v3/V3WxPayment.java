package top.chengdongqing.common.payment.wxpay.v3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IPayment;
import top.chengdongqing.common.payment.PayClient;
import top.chengdongqing.common.payment.PaymentDetails;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.WxStatus;
import top.chengdongqing.common.payment.wxpay.v3.callback.CallbackResource;
import top.chengdongqing.common.payment.wxpay.v3.callback.WxCallback;
import top.chengdongqing.common.payment.wxpay.v3.kit.DecryptKit;
import top.chengdongqing.common.payment.wxpay.v3.kit.V3SignatureKit;
import top.chengdongqing.common.payment.wxpay.v3.reqpay.RequestPaymentContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 微信支付
 * V3
 *
 * @author Luyao
 */
@Component
public class V3WxPayment implements IPayment {

    @Autowired
    private WxConstants constants;
    @Autowired
    private V3Constants v3constants;

    @Override
    public Ret requestPayment(PaymentRequestEntity entity, PayClient client) {
        return new RequestPaymentContext(client).request(entity);
    }

    @Override
    public Ret handleCallback(Map<String, String> params) {
        if (params.isEmpty()) return toFailJson("参数错误");
        // 将弱类型MAP转强类型对象
        WxCallback wxCallback = WxCallback.of(params);

        // 验证签名
        boolean verify = V3SignatureKit.verify(wxCallback, v3constants.getPublicKey());
        if (!verify) return toFailJson("验签失败");

        // 获取请求体中的加密数据
        String resourceStr = JSON.parseObject(wxCallback.getBody()).getString("resource");
        CallbackResource callbackResource = JSON.parseObject(resourceStr, CallbackResource.class);
        // 解密数据
        CallbackResource.Resource resource = DecryptKit.decrypt(callbackResource, constants.getMchId());

        // 判断支付结果
        if (!resource.isTradeSuccess()) return toFailJson("交易失败");

        // 封装支付信息
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .orderNo(resource.getOutTradeNo())
                .paymentNo(resource.getTransactionId())
                // 将单位从分转为元
                .paymentAmount(new BigDecimal(resource.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100)))
                // 转换支付时间
                .paymentTime(LocalDateTime.parse(resource.getSuccessTime(), DateTimeFormatter.ISO_ZONED_DATE_TIME))
                .build();
        // 返回回调结果
        JSONObject response = new JSONObject();
        response.put("code", WxStatus.SUCCESS);
        return Ret.ok(CallbackResponseEntity.builder()
                .json(JSON.toJSONString(response))
                .details(paymentDetails)
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
        JSONObject response = new JSONObject();
        response.put("code", WxStatus.FAIL);
        response.put("message", errorMsg);
        return Ret.fail(response.toJSONString());
    }

    @Override
    public Ret requestClose(String orderNo) {
        return null;
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
        private PaymentDetails details;
    }
}
