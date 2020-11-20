package top.chengdongqing.common.pay.wxpay.v2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.pay.IPayer;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.entities.PayResEntity;
import top.chengdongqing.common.pay.entities.RefundReqEntity;
import top.chengdongqing.common.pay.entities.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxpayConfigs;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.WxpayStatus;
import top.chengdongqing.common.pay.wxpay.v2.reqer.WxpayReqerHolderV2;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 微信支付
 * V2
 *
 * @author Luyao
 */
@Slf4j
@Component
public class WxpayerV2 implements IPayer {

    @Autowired
    private WxpayConfigs configs;
    @Autowired
    private WxpayConfigsV2 v2configs;
    @Autowired
    private WxpayHelper helper;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        return new WxpayReqerHolderV2(tradeType).request(entity);
    }

    @Override
    public Ret<Void> requestClose(String orderNo, TradeType tradeType) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("appid", helper.getAppId(tradeType))
                .add("mch_id", configs.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("out_trade_no", orderNo)
                .add("key", v2configs.getSecretKey())
                .add("sign_type", v2configs.getSignType());
        String sign = DigitalSigner.signature(
                SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2configs.getSecretKey()).fromHex())
                .toHex();
        params.add("sign", sign);
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.toXml(params);
        // 发送请求
        String requestUrl = helper.buildRequestUrl(v2configs.getRequestApi().getClose());
        String result = HttpKit.post(requestUrl, xml).body();
        log.info("请求订单关闭，参数：{}，\n结果：{}", xml, result);
        // 判断结果
        return WxpayHelperV2.getResult(XmlKit.parseXml(result));
    }

    @Override
    public Ret<Void> requestRefund(RefundReqEntity entity, TradeType tradeType) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("appid", helper.getAppId(tradeType))
                .add("mch_id", configs.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("out_trade_no", entity.getOrderNo())
                .add("out_refund_no", entity.getRefundNo())
                .add("total_fee", WxpayHelper.convertAmount(entity.getTotalAmount()) + "")
                .add("refund_fee", WxpayHelper.convertAmount(entity.getRefundAmount()) + "")
                .add("key", v2configs.getSecretKey())
                .add("sign_type", v2configs.getSignType());
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2configs.getSecretKey()).fromHex())
                .toHex();
        params.add("sign", sign);
        params.remove("key");

        try {
            // 转换数据类型
            String xml = XmlKit.toXml(params);
            // 读取证书
            byte[] certBytes = Files.readAllBytes(Paths.get(v2configs.getCertPath()));
            // 发送请求
            String requestUrl = helper.buildRequestUrl(v2configs.getRequestApi().getRefund());
            String result = HttpKit.post(requestUrl, xml, certBytes, configs.getMchId()).body();
            log.info("请求订单退款，参数：{}，\n结果：{}", xml, result);
            // 判断结果
            return WxpayHelperV2.getResult(XmlKit.parseXml(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ret<TradeQueryEntity> requestQuery(String orderNo, TradeType tradeType) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("appid", helper.getAppId(tradeType))
                .add("mch_id", configs.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("out_trade_no", orderNo)
                .add("key", v2configs.getSecretKey())
                .add("sign_type", v2configs.getSignType());
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2configs.getSecretKey()).fromHex())
                .toHex();
        params.add("sign", sign);
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.toXml(params);
        // 发送请求
        String requestUrl = helper.buildRequestUrl(v2configs.getRequestApi().getQuery());
        String result = HttpKit.post(requestUrl, xml).body();
        log.info("请求查询订单，参数：{}，\n结果：{}", xml, result);

        // 转换数据类型
        Map<String, String> resultMap = XmlKit.parseXml(result);
        // 判断请求结果
        Ret<TradeQueryEntity> queryResult = WxpayHelperV2.getResult(resultMap);
        if (queryResult.isFail()) return queryResult;

        // 封装响应数据
        TradeQueryEntity tradeQueryEntity = TradeQueryEntity.builder()
                .orderNo(resultMap.get("out_trade_no"))
                .paymentNo(resultMap.get("transaction_id"))
                .tradeTime(WxpayHelperV2.convertTime(resultMap.get("time_end")))
                .tradeAmount(WxpayHelper.convertAmount(Integer.parseInt(resultMap.get("total_fee"))))
                .tradeChannel(TradeChannel.WXPAY)
                .tradeType(WxpayHelper.getTradeType(resultMap.get("trade_type")))
                .tradeState(WxpayHelper.getTradeState(resultMap.get("trade_state")))
                .build();
        return Ret.ok(tradeQueryEntity);
    }

    /**
     * 处理支付回调
     *
     * @param xml 回调数据
     * @return 处理结果
     */
    public Ret<PayResEntity> handlePayCallback(String xml) {
        // 将xml转为map
        Map<String, String> params = XmlKit.parseXml(xml);
        // 判断参数是否为空
        if (params == null || params.isEmpty() || StringUtils.isBlank(params.get("sign"))) {
            throw new IllegalArgumentException("wx callback params is error");
        }

        // 验证签名
        params.put("key", v2configs.getSecretKey());
        boolean isOk = DigitalSigner.verify(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2configs.getSecretKey()).fromHex(),
                StrToBytes.of(params.get("sign")).fromHex());
        if (!isOk) return buildFailCallback("验签失败");

        // 判断支付结果
        if (!WxpayStatus.isOk(params.get("result_code"))) return buildFailCallback("支付失败");

        // 封装支付信息
        PayResEntity payResEntity = PayResEntity.builder()
                .orderNo(params.get("out_trade_no"))
                .paymentNo(params.get("transaction_id"))
                // 将单位从分转为元
                .paymentAmount(WxpayHelper.convertAmount(Integer.parseInt(params.get("total_fee"))))
                // 转换支付时间
                .paymentTime(WxpayHelperV2.convertTime(params.get("time_end")))
                .build();
        // 返回回调结果
        Kv<String, String> map = Kv.of("return_code", WxpayStatus.SUCCESS);
        return Ret.ok(payResEntity, XmlKit.toXml(map));
    }

    /**
     * 构建失败回调响应
     *
     * @param errorMsg 错误信息
     * @return 带xml的处理结果
     */
    private Ret<PayResEntity> buildFailCallback(String errorMsg) {
        Kv<String, String> map = Kv.of("return_code", WxpayStatus.FAIL).add("return_msg", errorMsg);
        return Ret.fail(XmlKit.toXml(map));
    }
}
