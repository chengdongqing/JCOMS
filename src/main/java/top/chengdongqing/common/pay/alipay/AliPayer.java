package top.chengdongqing.common.pay.alipay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayer;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.entities.RefundReqEntity;
import top.chengdongqing.common.pay.entities.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeType;

/**
 * 支付宝支付
 *
 * @author Luyao
 */
public class AliPayer implements IPayer {

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        return null;
    }

    @Override
    public Ret<Void> requestClose(String orderNo, TradeType tradeType) {
        return null;
    }

    @Override
    public Ret<Void> requestRefund(RefundReqEntity entity, TradeType tradeType) {
        return null;
    }

    @Override
    public Ret<TradeQueryEntity> requestQuery(String orderNo, TradeType tradeType) {
        return null;
    }
}
