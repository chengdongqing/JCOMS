package top.chengdongqing.common.pay.alipay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayer;
import top.chengdongqing.common.pay.alipay.reqer.*;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.entity.RefundReqEntity;
import top.chengdongqing.common.pay.entity.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.enums.TradeType;

import java.math.BigDecimal;

/**
 * 支付宝支付
 *
 * @author Luyao
 */
@Slf4j
@Component
public class Alipayer extends ApplicationObjectSupport implements IPayer {

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
                    .tradeState(helper.getTradeState(response.get("trade_status")))
                    .tradeTime(AlipayHelper.convertTime(response.get("send_pay_date")))
                    .build();
            return Ret.ok(tradeQueryEntity);
        } catch (IllegalStateException e) {
            return Ret.fail(e.getMessage());
        }
    }
}
