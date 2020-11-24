package top.chengdongqing.common.pay.alipay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayment;
import top.chengdongqing.common.pay.entity.PayResEntity;

import java.util.Map;

/**
 * 支付宝支付处理器
 *
 * @author Luyao
 */
public interface IAlipay extends IPayment {

    /**
     * 处理支付回调
     *
     * @param params 回调数据
     * @return 处理结果
     */
    Ret<PayResEntity> handlePayCallback(Map<String, String[]> params);
}
