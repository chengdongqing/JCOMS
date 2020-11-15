package top.chengdongqing.common.payment.wxpay.v3.callback;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entities.PayResEntity;
import top.chengdongqing.common.payment.entities.RefundResEntity;
import top.chengdongqing.common.payment.wxpay.v3.callback.entities.CallbackEntity;

/**
 * 微信v3回调处理器
 *
 * @author Luyao
 */
public interface ICallbackHandler {

    /**
     * 处理支付回调
     *
     * @param entity 回调数据实体
     * @return 处理结果
     */
    Ret<PayResEntity> handlePayCallback(CallbackEntity entity);

    /**
     * 处理退款回调
     *
     * @param entity 回调数据实体
     * @return 处理结果
     */
    Ret<RefundResEntity> handleRefundCallback(CallbackEntity entity);
}
