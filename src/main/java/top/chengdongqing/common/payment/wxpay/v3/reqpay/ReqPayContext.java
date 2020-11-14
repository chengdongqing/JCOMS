package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import org.springframework.context.support.ApplicationObjectSupport;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IReqPay;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;

/**
 * 请求付款实例上下文
 * v3
 *
 * @author Luyao
 */
public class ReqPayContext extends ApplicationObjectSupport {

    private final IReqPay strategy;

    /**
     * 根据请求客户端获取请求实例
     *
     * @param tradeType 交易类型
     */
    public ReqPayContext(TradeType tradeType) {
        Class<? extends IReqPay> clazz = switch (tradeType) {
            case APP -> APPReqPay.class;
            case MWEB -> MBReqPay.class;
            case JSAPI -> MPReqPay.class;
            case NATIVE -> PCReqPay.class;
        };
        strategy = super.getApplicationContext().getBean(clazz);
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
