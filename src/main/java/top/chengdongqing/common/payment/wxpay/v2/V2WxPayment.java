package top.chengdongqing.common.payment.wxpay.v2;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.kit.XmlKit;
import top.chengdongqing.common.payment.IPayment;
import top.chengdongqing.common.payment.PayClient;
import top.chengdongqing.common.payment.PaymentDetails;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.wxpay.WxStatus;
import top.chengdongqing.common.signature.Bytes;
import top.chengdongqing.common.signature.HMacSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付
 * V2
 *
 * @author Luyao
 */
@Slf4j
@Component
public class V2WxPayment implements IPayment {

    @Autowired
    private V2WxConstants constants;

    @Override
    public Ret requestPayment(PaymentRequestEntity entity, PayClient client) {
        return new RequestPaymentContext(client).request(entity);
    }

    @Override
    public Ret handleCallback(Map<String, String> params) {
        String sign = params.get("sign");
        if (params.isEmpty() || StringUtils.isBlank(sign)) {
            return toFailXml("参数错误");
        }

        // 验证签名
        params.put("key", constants.getSecretKey());
        boolean isOk = HMacSigner.verifyForHex(StrKit.buildQueryStr(params), constants.getSecretKey(), SignatureAlgorithm.HMAC_SHA256, sign);
        if (!isOk) return toFailXml("验签失败");

        // 判断支付结果
        if (!WxStatus.isOk(params.get("result_code"))) return toFailXml("支付失败");

        // 封装支付信息
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .orderNo(params.get("out_trade_no"))
                .paymentNo(params.get("transaction_id"))
                // 将单位从分转为元
                .paymentAmount(new BigDecimal(params.get("total_fee")).divide(BigDecimal.valueOf(100)))
                // 转换支付时间
                .paymentTime(LocalDateTime.parse(params.get("time_end"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .build();
        // 返回回调结果
        Map<String, String> map = new HashMap<>();
        map.put("return_code", WxStatus.SUCCESS);
        return Ret.ok(CallbackResponseEntity.builder()
                .xml(XmlKit.mapToXml(map))
                .details(paymentDetails)
                .build()
        );
    }

    /**
     * 获取微信回调失败需要响应的xml
     * 只有回调校验失败才调用
     *
     * @param errorMsg 错误信息
     * @return 带xml的处理结果
     */
    private Ret toFailXml(String errorMsg) {
        Map<String, String> params = new HashMap<>();
        params.put("return_code", WxStatus.FAIL);
        params.put("return_msg", errorMsg);
        return Ret.fail(XmlKit.mapToXml(params));
    }

    @Override
    public Ret requestClose(String orderNo) {
        // 封装请求参数
        Map<String, String> params = new HashMap<>();
        params.put("appid", constants.getAppId().getMp());
        params.put("mch_id", constants.getMchId());
        params.put("nonce_str", StrKit.getRandomUUID());
        params.put("out_trade_no", orderNo);
        params.put("key", constants.getSecretKey());
        params.put("sign_type", constants.getSignType());
        Bytes sign = HMacSigner.signatureForHex(StrKit.buildQueryStr(params), constants.getSecretKey(), SignatureAlgorithm.HMAC_SHA256);
        params.put("sign", sign.toHex());
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.mapToXml(params);
        // 发送请求
        log.info("发送关闭订单请求：{}", xml);
        String result = HttpKit.post(constants.getCloseUrl(), xml);
        log.info("请求关闭订单结果：{}", result);
        // 判断结果
        return getResult(XmlKit.xmlToMap(result));
    }

    @Override
    public Ret requestRefund(String orderNo, String refundNo, BigDecimal totalAmount, BigDecimal refundAmount) {
        // 将金额的单位从元转为分
        String totalFee = String.valueOf(totalAmount.multiply(BigDecimal.valueOf(100)).intValue());
        String refundFee = String.valueOf(refundAmount.multiply(BigDecimal.valueOf(100)).intValue());

        // 封装请求参数
        Map<String, String> params = new HashMap<>();
        params.put("appid", constants.getAppId().getMp());
        params.put("mch_id", constants.getMchId());
        params.put("nonce_str", StrKit.getRandomUUID());
        params.put("out_trade_no", orderNo);
        params.put("out_refund_no", refundNo);
        params.put("total_fee", totalFee);
        params.put("refund_fee", refundFee);
        params.put("key", constants.getSecretKey());
        params.put("sign_type", constants.getSignType());
        Bytes sign = HMacSigner.signatureForHex(StrKit.buildQueryStr(params), constants.getSecretKey(), SignatureAlgorithm.HMAC_SHA256);
        params.put("sign", sign.toHex());
        params.remove("key");

        // 获取证书文件流
        try (InputStream cert = Files.newInputStream(Paths.get(constants.getCertPath()))) {
            // 转换数据类型
            String xml = XmlKit.mapToXml(params);
            log.info("发送订单退款请求：{}", xml);
            // 发送请求
            String result = HttpKit.postWithCert(constants.getRefundUrl(), xml, cert, constants.getMchId());
            log.info("请求订单退款结果：{}", result);
            // 判断结果
            return getResult(XmlKit.xmlToMap(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证请求结果
     *
     * @param resultMap 响应信息
     * @return 验证结果
     */
    static Ret getResult(Map<String, String> resultMap) {
        boolean isOk = WxStatus.isOk(resultMap.get("return_code")) && WxStatus.isOk(resultMap.get("result_code"));
        return isOk ? Ret.ok() : Ret.fail(ErrorMsg.REQUEST_FAILED);
    }


    /**
     * 支付回调响应对象
     */
    @Data
    @Builder
    public static class CallbackResponseEntity {
        /**
         * 返回给微信服务器的xml
         */
        private String xml;
        /**
         * 收集的支付详情
         */
        private PaymentDetails details;
    }
}
