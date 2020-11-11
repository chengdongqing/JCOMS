package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IReqPay;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;

/**
 * @author Luyao
 */
public class ReqPayContext {

    private IReqPay strategy;

    /**
     * 根据请求客户端获取请求实例
     *
     * @param tradeType 请求支付客户端
     */
    public ReqPayContext(TradeType tradeType) {
        if (tradeType == TradeType.NATIVE) {
            strategy = new PCReqPay();
        } else if (tradeType == TradeType.APP) {
            strategy = new APPReqPay();
        } else if (tradeType == TradeType.JSAPI) {
            strategy = new MPReqPay();
        } else if (tradeType == TradeType.MWEB) {
            strategy = new MBReqPay();
        }
    }

    /**
     * 请求付款
     *
     * @param entity 参数实体
     * @return 请求响应
     */
    public Ret request(PayReqEntity entity) {
        return strategy.requestPayment(entity);
    }
}
