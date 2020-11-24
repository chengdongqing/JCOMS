package top.chengdongqing.common.pay.wxpay.v3;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayment;
import top.chengdongqing.common.pay.entity.PayResEntity;
import top.chengdongqing.common.pay.entity.RefundResEntity;
import top.chengdongqing.common.pay.wxpay.v3.entity.CallbackEntity;

/**
 * 微信支付处理器
 * v3
 *
 * @author Luyao
 */
public interface IWxpayV3 extends IPayment {

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
