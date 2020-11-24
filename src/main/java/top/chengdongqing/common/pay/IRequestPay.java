package top.chengdongqing.common.pay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.alipay.reqer.AlipayReqer;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.v2.reqer.WxpayReqerV2;
import top.chengdongqing.common.pay.wxpay.v3.reqer.WxpayReqerV3;

/**
 * 请求付款统一接口
 *
 * @author Luyao
 * @see AlipayReqer
 * @see WxpayReqerV2
 * @see WxpayReqerV3
 */
public interface IRequestPay {

    /**
     * 请求付款
     *
     * @param entity    参数实体
     * @param tradeType 交易类型
     * @return 响应数据
     */
    Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType);
}
