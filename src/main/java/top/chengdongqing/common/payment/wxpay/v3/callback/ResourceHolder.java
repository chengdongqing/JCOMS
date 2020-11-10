package top.chengdongqing.common.payment.wxpay.v3.callback;

import lombok.Data;
import top.chengdongqing.common.payment.wxpay.WxStatus;

/**
 * 微信支付回调数据
 *
 * @author Luyao
 */
@Data
public class ResourceHolder {

    /**
     * 加密算法
     */
    private String algorithm;
    /**
     * 附加数据
     */
    private String associatedData;
    /**
     * 随机数
     */
    private String nonce;
    /**
     * 核心数据密文
     */
    private String ciphertext;

    /**
     * 核心数据解密后的对象
     */
    @Data
    public static class Resource {

        /**
         * 微信平台的交易编号
         */
        private String transactionId;
        /**
         * 交易状态
         */
        private String tradeState;
        /**
         * 支付成功时间
         */
        private String successTime;
        /**
         * 订单号
         */
        private String outTradeNo;
        /**
         * 交易类型
         */
        private String tradeType;
        /**
         * 交易金额
         */
        private Amount amount;

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
        }

        /**
         * 是否交易成功
         */
        public boolean isTradeSuccess() {
            return WxStatus.isOk(this.getTradeState());
        }
    }
}