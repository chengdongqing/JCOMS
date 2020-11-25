package top.chengdongqing.common.pay.alipay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.CertKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayment;
import top.chengdongqing.common.pay.alipay.reqer.*;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.entity.PayResEntity;
import top.chengdongqing.common.pay.entity.RefundReqEntity;
import top.chengdongqing.common.pay.entity.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.enums.TradeState;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝支付
 *
 * @author Luyao
 */
@Slf4j
@Component
public class Alipay extends ApplicationObjectSupport implements IPayment<Map<String, String[]>> {

    @Autowired
    private AlipayConfigs configs;
    @Autowired
    private AlipayHelper helper;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        Class<? extends AlipayReqer> clazz = switch (tradeType) {
            case PC -> PCPayReqer.class;
            case MP -> MPPayReqer.class;
            case MB -> MBPayReqer.class;
            case APP -> APPPayReqer.class;
        };
        return super.getApplicationContext().getBean(clazz).requestPayment(entity, tradeType);
    }

    @Override
    public Ret<Void> requestClose(String orderNo, TradeType tradeType) {
        // 构建请求参数
        Kv<String, String> params = new Kv<>();
        helper.buildRequestParams(params, Kv.of("out_trade_no", orderNo).toJson(), configs.getMethod().getClose());
        // 发送关闭请求
        try {
            helper.requestAlipay(params, "close");
            return Ret.ok();
        } catch (Exception e) {
            return Ret.fail(e.getMessage());
        }
    }

    @Override
    public Ret<Void> requestRefund(RefundReqEntity entity, TradeType tradeType) {
        // 构建请求参数
        Kv<String, String> params = new Kv<>();
        String bizContent = Kv.ofAny("out_trade_no", entity.getOrderNo())
                .add("refund_amount", entity.getRefundAmount())
                .add("refund_reason", entity.getReason())
                .add("out_request_no", entity.getRefundNo()).toJson();
        helper.buildRequestParams(params, bizContent, configs.getMethod().getRefund());
        // 发送退款请求
        try {
            helper.requestAlipay(params, "refund");
            return Ret.ok();
        } catch (Exception e) {
            return Ret.fail(e.getMessage());
        }
    }

    @Override
    public Ret<TradeQueryEntity> requestQuery(String orderNo, TradeType tradeType) {
        // 构建请求参数
        Kv<String, String> params = new Kv<>();
        helper.buildRequestParams(params, Kv.of("out_trade_no", orderNo).toJson(), configs.getMethod().getQuery());
        // 发送查询请求
        try {
            Kv<String, String> response = helper.requestAlipay(params, "query");
            TradeQueryEntity tradeQueryEntity = TradeQueryEntity.builder()
                    .orderNo(response.get("out_trade_no"))
                    .paymentNo(response.get("trade_no"))
                    .tradeAmount(new BigDecimal(response.get("total_amount")))
                    .tradeChannel(TradeChannel.ALIPAY)
                    .tradeType(tradeType)
                    .tradeState(TradeState.ofAlipayCode(response.get("trade_status")))
                    .tradeTime(AlipayHelper.convertTime(response.get("send_pay_date")))
                    .build();
            return Ret.ok(tradeQueryEntity);
        } catch (IllegalStateException e) {
            return Ret.fail(e.getMessage());
        }
    }

    @Override
    public Ret<PayResEntity> handlePayCallback(Map<String, String[]> paraMap) {
        // 转换数据类型
        Kv<String, String> params = Kv.of(paraMap);
        if (params.isEmpty() || !params.containsKey("sign")) {
            throw new IllegalArgumentException("alipay callback params is error");
        }

        // 验证签名
        boolean isOk = DigitalSigner.verify(SignatureAlgorithm.RSA_SHA256,
                helper.buildQueryStr(params),
                CertKit.readPublicKey(configs.getAlipayCertPath()).bytes(),
                StrToBytes.of(params.get("sign")).fromBase64());
        if (!isOk) {
            log.warn("支付宝回调验签失败：{}", params);
            return Ret.fail(AlipayStatus.CALLBACK_CODE);
        }

        // 校验支付结果
        if (!AlipayStatus.isOk(params.get("trade_status"))) {
            return Ret.fail(AlipayStatus.CALLBACK_CODE);
        }

        // 封装支付信息
        PayResEntity payResEntity = PayResEntity.builder()
                .orderNo(params.get("out_trade_no"))
                .paymentNo(params.get("trade_no"))
                .paymentAmount(new BigDecimal(params.get("total_amount")))
                .paymentTime(helper.convertTime(params.get("gmt_payment")))
                .build();
        // 返回回调结果
        return Ret.ok(payResEntity, AlipayStatus.CALLBACK_CODE);
    }
}
