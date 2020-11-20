package top.chengdongqing.common.pay.wxpay.v3.reqer;

import org.springframework.context.support.ApplicationObjectSupport;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayReqer;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;

/**
 * 请求付款实例上下文
 * v3
 *
 * @author Luyao
 */
public class ReqerHolder extends ApplicationObjectSupport {

    private final IPayReqer strategy;

    /**
     * 根据请求客户端获取请求实例
     *
     * @param tradeType 交易类型
     */
    public ReqerHolder(TradeType tradeType) {
        Class<? extends IPayReqer> clazz = switch (tradeType) {
            case APP -> APPPayReqer.class;
            case MB -> MBPayReqer.class;
            case MP -> MPPayReqer.class;
            case PC -> PCPayReqer.class;
        };
        strategy = super.getApplicationContext().getBean(clazz);
    }

    /**
     * 请求付款
     *
     * @param entity 参数实体
     * @return 请求响应
     */
    public Ret<Object> request(PayReqEntity entity) {
        return strategy.requestPayment(entity);
    }
}
