package top.chengdongqing.common.payment;

import top.chengdongqing.common.kit.Ret;

/**
 * 请求付款接口
 *
 * @author James Lu
 */
public interface IRequestPayment {

    Ret requestPayment(PaymentRequestEntity entity);
}
