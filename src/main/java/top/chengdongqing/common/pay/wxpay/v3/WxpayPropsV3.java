package top.chengdongqing.common.pay.wxpay.v3;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置
 * v3
 *
 * @author Luyao
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("pay.wx.v3")
public class WxpayPropsV3 {

    /**
     * 微信接口请求地址
     */
    @Autowired
    private WxpayRequestApiV3 requestApi;

    // 认证方案
    private String authSchema;
    // 应用私钥
    private String privateKey;
    // 应用公钥
    private String publicKey;
    // 微信公钥证书路径
    private String wxpayCertPath;
    // 应用公钥证书路径
    private String appCertPath;

    /**
     * AES解密密钥
     */
    private String key;

    /**
     * 币种
     */
    private String currency;

    // 支付回调地址
    private String paymentNotifyUrl;
    // 退款回调地址
    private String refundNotifyUrl;

    @Getter
    @Component
    public static class WxpayRequestApiV3 {

        // 请求付款接口
        @Autowired
        private WxpayTradeApiV3 pay;
        // 关闭订单接口
        @Value("/out-trade-no/%s/close")
        private String close;
        // 查询订单接口
        @Value("/out-trade-no/%s")
        private String query;
        // 请求退款接口
        @Value("/v3/ecommerce/refunds/apply")
        private String refund;

        @Getter
        @Component
        public static class WxpayTradeApiV3 {

            @Value("/v3/pay/transactions/native")
            private String pc;
            @Value("/v3/pay/transactions/jsapi")
            private String mp;
            @Value("/v3/pay/transactions/app")
            private String app;
            @Value("/v3/pay/transactions/h5")
            private String mb;
        }
    }

    /**
     * 二级商户APPID
     */
    private String subAppId;
    /**
     * 二级商户号
     */
    private String subMchId;
    /**
     * 电商平台APPID
     */
    private String spAppId;
}
