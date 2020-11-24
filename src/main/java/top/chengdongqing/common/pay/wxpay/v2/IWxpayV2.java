package top.chengdongqing.common.pay.wxpay.v2;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayment;
import top.chengdongqing.common.pay.entity.PayResEntity;

/**
 * 微信支付处理器
 * v2
 *
 * @author Luyao
 */
public interface IWxpayV2 extends IPayment {

    /**
     * 处理支付回调
     *
     * @param xml 回调数据
     * @return 处理结果
     */
    Ret<PayResEntity> handlePayCallback(String xml);
}
