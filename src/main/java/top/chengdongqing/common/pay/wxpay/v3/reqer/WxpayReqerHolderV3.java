package top.chengdongqing.common.pay.wxpay.v3.reqer;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
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
@Component
public class WxpayReqerHolderV3 extends ApplicationObjectSupport {

    private final IPayReqer context;
    private final TradeType tradeType;

    /**
     * 根据请求客户端获取请求实例
     *
     * @param tradeType 交易类型
     */
    public WxpayReqerHolderV3(TradeType tradeType) {
        Class<? extends IPayReqer> clazz = switch (tradeType) {
            case APP -> APPPayReqerV3.class;
            case MB -> MBPayReqerV3.class;
            case MP -> MPPayReqerV3.class;
            case PC -> PCPayReqerV3.class;
        };
        context = super.getApplicationContext().getBean(clazz);
        this.tradeType = tradeType;
    }

    /**
     * 请求付款
     *
     * @param entity 参数实体
     * @return 请求响应
     */
    public Ret<Object> request(PayReqEntity entity) {
        return context.requestPayment(entity, tradeType);
    }
}
