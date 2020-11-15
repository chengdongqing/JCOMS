package top.chengdongqing.common.payment.wxpay.v3.callback.entities;

import lombok.Data;

/**
 * 退款回调数据实体
 *
 * @author Luyao
 */
@Data
public class RefundCallbackEntity {

    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * 退款单号
     */
    private String outRefundNo;
    /**
     * 微信退款单号
     */
    private String refundId;
    /**
     * 退款状态
     */
    private RefundStatus refundStatus;
    /**
     * 退款成功时间
     */
    private String successTime;
    /**
     * 金额信息
     */
    private Amount amount;

    /**
     * 退款状态
     */
    public enum RefundStatus {

        /**
         * 退款成功
         */
        SUCCESS,
        /**
         * 退款关闭
         */
        CLOSE,
        /**
         * 退款异常
         */
        ABNORMAL
    }

    @Data
    public static class Amount {
        /**
         * 用户实际支付总金额
         */
        private Integer payerTotal;
        /**
         * 订单总金额
         */
        private Integer total;
        /**
         * 用户退款总金额
         */
        private Integer payerRefund;
        /**
         * 退款金额
         */
        private Integer refund;
    }
}
