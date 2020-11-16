package top.chengdongqing.common.payment;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entities.PayReqEntity;
import top.chengdongqing.common.payment.entities.QueryResEntity;
import top.chengdongqing.common.payment.entities.RefundReqEntity;
import top.chengdongqing.common.payment.enums.TradeType;

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
    Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType);

    /**
     * 请求关闭订单
     *
     * @param orderNo 订单号
     * @return 关闭结果
     */
    Ret<Void> requestClose(String orderNo);

    /**
     * 请求订单退款
     *
     * @param entity 参数实体
     * @return 退款结果
     */
    Ret<Void> requestRefund(RefundReqEntity entity);

    /**
     * 请求查询订单
     *
     * @param orderNo 订单号
     * @return 查询结果
     */
    Ret<QueryResEntity> requestQuery(String orderNo);
}
