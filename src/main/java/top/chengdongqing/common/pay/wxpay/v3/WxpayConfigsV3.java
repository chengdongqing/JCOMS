package top.chengdongqing.common.pay.wxpay.v3;

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
@Getter
@Component
@RefreshScope
@ConfigurationProperties("pay.wx.v3")
public class WxpayConfigsV3 {

    /**
     * 认证类型描述
     */
    @Value("WECHATPAY2-SHA256-RSA2048")
    private String authSchema;
    /**
     * 证书序列号
     */
    private String certSerialNo;

    /**
     * 微信接口请求地址
     */
    @Autowired
    private WxpayRequestApiV3 requestApi;

    /**
     * 签名私钥
     */
    private String privateKey;
    /**
     * 验签公钥
     */
    private String publicKey;

    /**
     * 解密密钥
     */
    private String secretKey;

    /**
     * 币种
     */
    @Value("CNY")
    private String currency;

    /**
     * 回调地址
     */
    @Autowired
    private WxpayNotifyUrlV3 notifyUrl;

    @Getter
    @Component
    @RefreshScope
    @ConfigurationProperties("pay.wx.v3.notify-url")
    public static class WxpayNotifyUrlV3 {

        // 支付回调地址
        private String payment;
        // 退款回调地址
        private String refund;
    }

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
        @ConfigurationProperties("/v3/pay/transactions")
        public static class WxpayTradeApiV3 {

            @Value("/native")
            private String pc;
            @Value("/jsapi")
            private String mp;
            @Value("/app")
            private String app;
            @Value("/h5")
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
