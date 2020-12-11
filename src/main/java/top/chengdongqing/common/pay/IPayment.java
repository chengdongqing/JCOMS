package top.chengdongqing.common.pay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.entity.PayResEntity;
import top.chengdongqing.common.pay.entity.RefundReqEntity;
import top.chengdongqing.common.pay.entity.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeType;

/**
 * 支付处理器顶层接口
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
     * @param orderNo   订单号
     * @param tradeType 交易类型
     * @return 关闭结果
     */
    Ret<Void> requestClose(String orderNo, TradeType tradeType);

    /**
     * 请求订单退款
     *
     * @param entity    参数实体
     * @param tradeType 交易类型
     * @return 退款结果
     */
    Ret<Void> requestRefund(RefundReqEntity entity, TradeType tradeType);

    /**
     * 请求查询订单
     *
     * @param orderNo   订单号
     * @param tradeType 交易类型
     * @return 查询结果
     */
    Ret<TradeQueryEntity> requestQuery(String orderNo, TradeType tradeType);

    /**
     * 处理支付回调
     *
     * @param data 回调数据
     * @return 处理结果
     */
    Ret<PayResEntity> handlePayCallback(Object data);
}
