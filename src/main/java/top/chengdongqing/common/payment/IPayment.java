package top.chengdongqing.common.payment;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.entity.RefundReqEntity;

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
     * @param tradeType 交易类型
     * @return 响应数据
     */
    Ret requestPayment(PayReqEntity entity, TradeType tradeType);

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
     * @param entity 参数实体
     * @return 退款结果
     */
    Ret requestRefund(RefundReqEntity entity);

    /**
     * 请求查询订单
     *
     * @param orderNo 订单号
     * @return 查询结果
     */
    Ret requestQuery(String orderNo);
}
