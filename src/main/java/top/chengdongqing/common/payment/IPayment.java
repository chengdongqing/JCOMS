package top.chengdongqing.common.payment;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entity.PayReqEntity;

import java.math.BigDecimal;

/**
 * 支付顶层接口
 *
 * @author Luyao
 */
public interface IPayment {

    /**
     * 发起付款
     *
     * @param entity    参数实体
     * @param tradeType 客户端类型
     * @return 响应数据
     */
    Ret requestPayment(PayReqEntity entity, TradeType tradeType);

    /**
     * 处理付款成功回调
     *
     * @param params 回调携带的参数
     * @return 响应数据
     */
    Ret handleCallback(Kv<String, String> params);

    /**
     * 请求关闭订单
     *
     * @param orderNo 订单号
     * @return 关闭结果
     */
    Ret requestClose(String orderNo);

    /**
     * 请求订单退款
     *
     * @param orderNo      订单号
     * @param refundNo     退款单号
     * @param totalAmount  订单总金额
     * @param refundAmount 要退款的金额
     * @return 退款结果
     */
    Ret requestRefund(String orderNo, String refundNo, BigDecimal totalAmount, BigDecimal refundAmount);
}
