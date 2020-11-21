package top.chengdongqing.common.pay.wxpay.v2.reqer;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayReqer;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;

/**
 * 请求付款实例上下文
 * v2
 *
 * @author Luyao
 */
@Component
public class WxpayReqerHolderV2 extends ApplicationObjectSupport {

    private final IPayReqer context;

    /**
     * 根据请求客户端获取请求实例
     *
     * @param tradeType 交易类型
     */
    public WxpayReqerHolderV2(TradeType tradeType) {
        Class<? extends IPayReqer> clazz = switch (tradeType) {
            case APP -> APPPayReqerV2.class;
            case MB -> MBPayReqerV2.class;
            case MP -> MPPayReqerV2.class;
            case PC -> PCPayReqerV2.class;
        };
        context = super.getApplicationContext().getBean(clazz);
    }

    /**
     * 请求付款
     *
     * @param entity 参数实体
     * @return 请求响应
     */
    public Ret<Object> request(PayReqEntity entity) {
        return context.requestPayment(entity);
    }
}
