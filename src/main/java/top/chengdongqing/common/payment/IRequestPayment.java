package top.chengdongqing.common.payment;

import top.chengdongqing.common.kit.Ret;

/**
 * 请求付款接口
 *
 * @author Luyao
 */
public interface IRequestPayment {

    /**
     * 请求付款
     *
     * @param entity 请求参数实体
     * @return 请求结果
     */
    Ret requestPayment(PaymentRequestEntity entity);
}
