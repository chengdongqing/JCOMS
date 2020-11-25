package top.chengdongqing.common.pay.wxpay.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.XmlKit;
import top.chengdongqing.common.pay.IPayment;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.entity.PayResEntity;
import top.chengdongqing.common.pay.entity.RefundReqEntity;
import top.chengdongqing.common.pay.entity.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.enums.TradeState;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxpayConfigs;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.WxpayStatus;
import top.chengdongqing.common.pay.wxpay.v2.reqer.*;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 微信支付
 * V2
 *
 * @author Luyao
 */
@Slf4j
@Component
public class WxpayV2 extends ApplicationObjectSupport implements IPayment<String> {

    @Autowired
    private WxpayConfigs configs;
    @Autowired
    private WxpayConfigsV2 v2configs;
    @Autowired
    private WxpayHelper helper;
    @Autowired
    private WxpayHelperV2 helperV2;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        Class<? extends WxpayReqerV2> clazz = switch (tradeType) {
            case APP -> APPPayReqerV2.class;
            case MB -> MBPayReqerV2.class;
            case MP -> MPPayReqerV2.class;
            case PC -> PCPayReqerV2.class;
        };
        return super.getApplicationContext().getBean(clazz).requestPayment(entity, tradeType);
    }

    @Override
    public Ret<Void> requestClose(String orderNo, TradeType tradeType) {
        // 构建请求xml
        Kv<String, String> params = Kv.of("out_trade_no", orderNo);
        String xml = helperV2.buildRequestXml(tradeType, params);
        // 发送请求
        String requestUrl = helper.buildRequestUrl(v2configs.getRequestApi().getClose());
        String result = HttpKit.post(requestUrl, xml).body();
        log.info("请求订单关闭，参数：{}，\n结果：{}", xml, result);
        // 判断结果
        return WxpayHelperV2.getResult(XmlKit.parseXml(result));
    }

    @Override
    public Ret<Void> requestRefund(RefundReqEntity entity, TradeType tradeType) {
        // 构建请求xml
        Kv<String, String> params = Kv.of("out_trade_no", entity.getOrderNo())
                .add("out_refund_no", entity.getRefundNo())
                .add("total_fee", WxpayHelper.convertAmount(entity.getTotalAmount()).toString())
                .add("refund_fee", WxpayHelper.convertAmount(entity.getRefundAmount()).toString());
        String xml = helperV2.buildRequestXml(tradeType, params);

        try {
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
        Kv<String, String> params = Kv.of("out_trade_no", orderNo);
        String xml = helperV2.buildRequestXml(tradeType, params);

        // 发送请求
        String requestUrl = helper.buildRequestUrl(v2configs.getRequestApi().getQuery());
        String result = HttpKit.post(requestUrl, xml).body();
        log.info("请求查询订单，参数：{}，\n结果：{}", xml, result);

        // 转换数据类型
        Kv<String, String> response = XmlKit.parseXml(result);
        // 判断请求结果
        Ret<TradeQueryEntity> queryResult = WxpayHelperV2.getResult(response);
        if (queryResult.isFail()) return queryResult;

        // 封装响应数据
        TradeQueryEntity tradeQueryEntity = TradeQueryEntity.builder()
                .orderNo(response.get("out_trade_no"))
                .paymentNo(response.get("transaction_id"))
                .tradeTime(WxpayHelperV2.convertTime(response.get("time_end")))
                .tradeAmount(WxpayHelper.convertAmount(Integer.parseInt(response.get("total_fee"))))
                .tradeChannel(TradeChannel.WXPAY)
                .tradeType(TradeType.ofWxpayCode(response.get("trade_type")))
                .tradeState(TradeState.ofAlipayCode(response.get("trade_state")))
                .build();
        return Ret.ok(tradeQueryEntity);
    }

    @Override
    public Ret<PayResEntity> handlePayCallback(String xml) {
        // 将xml转为map
        Kv<String, String> params = XmlKit.parseXml(xml);
        // 判断参数是否为空
        if (params.isEmpty() || !params.containsKey("sign")) {
            throw new IllegalArgumentException("wxpay callback params is error");
        }

        // 验证签名
        boolean isOk = DigitalSigner.verify(SignatureAlgorithm.HMAC_SHA256,
                helperV2.buildQueryStr(params),
                v2configs.getSecretKey().getBytes(),
                StrToBytes.of(params.get("sign")).fromHex());
        if (!isOk) {
            log.warn("微信支付回调验签失败：{}", params);
            return buildFailCallback("验签失败");
        }

        // 判断支付结果
        if (!WxpayStatus.isOk(params.get("result_code"))) return buildFailCallback("支付失败");

        // 封装支付信息
        PayResEntity payResEntity = PayResEntity.builder()
                .orderNo(params.get("out_trade_no"))
                .paymentNo(params.get("transaction_id"))
                .paymentAmount(WxpayHelper.convertAmount(Integer.parseInt(params.get("total_fee"))))
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
