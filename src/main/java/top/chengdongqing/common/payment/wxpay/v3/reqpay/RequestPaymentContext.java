package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IRequestPayment;
import top.chengdongqing.common.payment.PayClient;
import top.chengdongqing.common.payment.PaymentRequestEntity;

/**
 * @author Luyao
 */
public class RequestPaymentContext {

    private IRequestPayment strategy;

    private RequestPaymentContext() {}

    public RequestPaymentContext(PayClient client) {
        if (client == PayClient.PC) {
            strategy = new PCRequestPayment();
        } else if (client == PayClient.APP) {
            strategy = new APPRequestPayment();
        } else if (client == PayClient.MP) {
            strategy = new MPRequestPayment();
        } else if (client == PayClient.MB) {
            strategy = new MBRequestPayment();
        }
    }

    public Ret request(PaymentRequestEntity entity) {
        return strategy.requestPayment(entity);
    }
}
