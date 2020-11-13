package top.chengdongqing.common.payment.wxpay.v3;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Luyao
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "payment.wx.v3")
public class WxV3Constants {

    /**
     * 认证类型描述
     */
    @Value("${schema:WECHATPAY2-SHA256-RSA2048}")
    private String authSchema;
    /**
     * 证书序列号
     */
    private String certSerialNo;
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
    /**
     *币种
     */
    @Value("${currency:CNY}")
    private String currency;

    /**
     * 订单请求支付地址
     */
    private PaymentUrl paymentUrl;
    /**
     * 支付结果通知地址
     */
    private String payNotifyUrl;
    /**
     * 关闭订单请求地址
     */
    @Value("${close-url:/out-trade-no/%s/close}")
    private String closeUrl;
    /**
     * 订单退款请求地址
     */
    @Value("${refund-url:/v3/ecommerce/refunds/apply}")
    private String refundUrl;
    /**
     * 退款结果通知地址
     */
    private String refundNotifyUrl;

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

    @Data
    @Component
    @ConfigurationProperties("pay.wx.v3.pay-url")
    public static class PaymentUrl {

        @Value("${pc:native}")
        private String pc;
        @Value("${pc:jsapi}")
        private String mp;
        @Value("${pc:app}")
        private String app;
        @Value("${pc:h5}")
        private String h5;
    }
}
