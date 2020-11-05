package top.chengdongqing.common.payment.wxpay.v3;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.PayClient;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.IRequestPayment;

/**
 * 策略模式+工厂模式
 *
 * @author Luyao
 */
public class RequestPaymentContext {

    private IRequestPayment strategy;

    public RequestPaymentContext(PayClient client) {
        if (client == PayClient.PC) {
            strategy = new PCRequestPayment();
        }
    }

    public Ret request(PaymentRequestEntity entity) {
        return strategy.requestPayment(entity);
    }
}
