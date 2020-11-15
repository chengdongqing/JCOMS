package top.chengdongqing.common.payment;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entities.PayReqEntity;

/**
 * 请求付款接口
 * 针对多种支付客户端请求付款的统一接口定义
 *
 * @author Luyao
 */
public interface IReqPay {

    /**
     * 请求付款
     *
     * @param entity 请求参数实体
     * @return 请求结果
     */
    Ret<Object> requestPayment(PayReqEntity entity);
}
