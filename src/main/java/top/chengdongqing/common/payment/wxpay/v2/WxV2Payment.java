package top.chengdongqing.common.payment.wxpay.v2;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.payment.IPayment;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.entity.PayResEntity;
import top.chengdongqing.common.payment.entity.RefundReqEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;
import top.chengdongqing.common.payment.wxpay.WxStatus;
import top.chengdongqing.common.payment.wxpay.v2.reqpay.ReqPayContext;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 微信支付
 * V2
 *
 * @author Luyao
 */
@Slf4j
@Component
public class WxV2Payment implements IPayment {

    @Autowired
    private WxConstants constants;
    @Autowired
    private WxV2Constants v2constants;

    @Override
    public Ret requestPayment(PayReqEntity entity, TradeType tradeType) {
        return new ReqPayContext(tradeType).request(entity);
    }

    /**
     * 获取微信回调失败需要响应的xml
     * 只有回调校验失败才调用
     *
     * @param errorMsg 错误信息
     * @return 带xml的处理结果
     */
    private Ret buildFailXml(String errorMsg) {
        Kv<String, String> map = Kv.go("return_code", WxStatus.FAIL).add("return_msg", errorMsg);
        return Ret.fail(XmlKit.mapToXml(map));
    }

    @Override
    public Ret requestClose(String orderNo) {
        // 封装请求参数
        Kv<String, String> params = Kv.go("appid", constants.getAppId().getMp())
                .add("mch_id", constants.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("out_trade_no", orderNo)
                .add("key", v2constants.getSecretKey())
                .add("sign_type", v2constants.getSignType());
        String sign = DigitalSigner.signature(
                SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2constants.getSecretKey()).fromHex())
                .toHex();
        params.add("sign", sign);
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.mapToXml(params);
        // 发送请求
        String result = HttpKit.post(v2constants.getCloseUrl(), xml).body();
        log.info("请求订单关闭，参数：{}，\n结果：{}", xml, result);
        // 判断结果
        return getResult(XmlKit.xmlToMap(result));
    }

    @Override
    public Ret requestRefund(RefundReqEntity entity) {
        // 将金额的单位从元转为分
        int totalFee = entity.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue();
        int refundFee = entity.getRefundAmount().multiply(BigDecimal.valueOf(100)).intValue();

        // 封装请求参数
        Kv<String, String> params = Kv.go("appid", constants.getAppId().getMp())
                .add("mch_id", constants.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("out_trade_no", entity.getOrderNo())
                .add("out_refund_no", entity.getRefundNo())
                .add("total_fee", totalFee + "")
                .add("refund_fee", refundFee + "")
                .add("key", v2constants.getSecretKey())
                .add("sign_type", v2constants.getSignType());
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2constants.getSecretKey()).fromHex())
                .toHex();
        params.add("sign", sign);
        params.remove("key");

        // 获取证书文件流
        try {
            // 转换数据类型
            String xml = XmlKit.mapToXml(params);
            // 读取证书
            byte[] certBytes = Files.readAllBytes(Paths.get(v2constants.getCertPath()));
            // 发送请求
            String result = HttpKit.post(v2constants.getRefundUrl(), xml, certBytes, constants.getMchId()).body();
            log.info("请求订单退款，参数：{}，\n结果：{}", xml, result);
            // 判断结果
            return getResult(XmlKit.xmlToMap(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ret requestQuery(String orderNo) {
        return null;
    }

    /**
     * 处理支付回调
     *
     * @param xml 回调数据
     * @return 处理结果
     */
    public Ret handlePayCallback(String xml) {
        // 将xml转为map
        Map<String, String> params = XmlKit.xmlToMap(xml);
        // 判断参数是否为空
        if (params == null || params.isEmpty() || StringUtils.isBlank(params.get("sign"))) {
            throw new IllegalArgumentException("wx callback params is error");
        }

        // 验证签名
        params.put("key", v2constants.getSecretKey());
        boolean isOk = DigitalSigner.verify(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2constants.getSecretKey()).fromHex(),
                StrToBytes.of(params.get("sign")).fromHex());
        if (!isOk) return buildFailXml("验签失败");

        // 判断支付结果
        if (!WxStatus.isOk(params.get("result_code"))) return buildFailXml("支付失败");

        // 封装支付信息
        PayResEntity payResEntity = PayResEntity.builder()
                .orderNo(params.get("out_trade_no"))
                .paymentNo(params.get("transaction_id"))
                // 将单位从分转为元
                .paymentAmount(WxPayHelper.convertAmount(Integer.parseInt(params.get("total_fee"))))
                // 转换支付时间
                .paymentTime(LocalDateTime.parse(params.get("time_end"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .build();
        // 返回回调结果
        Kv<String, String> map = Kv.go("return_code", WxStatus.SUCCESS);
        return Ret.ok(PayCallbackResEntity.builder()
                .response(XmlKit.mapToXml(map))
                .payResEntity(payResEntity)
                .build());
    }

    /**
     * 验证请求结果
     *
     * @param resultMap 响应信息
     * @return 验证结果
     */
    public static Ret getResult(Map<String, String> resultMap) {
        boolean isOk = WxStatus.isOk(resultMap.get("return_code")) && WxStatus.isOk(resultMap.get("result_code"));
        return isOk ? Ret.ok() : Ret.fail(ErrorMsg.REQUEST_FAILED);
    }


    /**
     * 支付回调响应实体
     */
    @Data
    @Builder
    public static class PayCallbackResEntity {
        /**
         * 响应数据
         */
        private String response;
        /**
         * 收集的支付详情
         */
        private PayResEntity payResEntity;
    }
}
