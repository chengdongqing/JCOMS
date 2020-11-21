package top.chengdongqing.common.pay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;

/**
 * 付款请求器
 * 定义同支付平台下不同类型客户端请求支付统一接口
 *
 * @author Luyao
 */
public interface IPayReqer {

    /**
     * 请求付款
     *
     * @param entity    请求参数实体
     * @param tradeType 交易类型
     * @return 请求结果
     */
    Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType);
}
