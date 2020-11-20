package top.chengdongqing.common.pay.wxpay.v2;

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
@ConfigurationProperties(prefix = "payment.wx.v2")
public class WxV2Configs {

    /**
     * 订单请求支付地址
     */
    @Value("${payment-url:/pay/unifiedorder}")
    private String paymentUrl;
    /**
     * 关闭订单请求地址
     */
    @Value("${payment-url:/pay/closeorder}")
    private String closeUrl;
    /**
     * 订单退款请求地址
     */
    @Value("${payment-url:/secapi/pay/refund}")
    private String refundUrl;
    /**
     * 订单查询请求地址
     */
    private String queryUrl;

    /**
     * 支付成功通知地址
     */
    private String notifyUrl;

    /**
     * 签名用的密钥
     */
    private String secretKey;

    /**
     * 证书地址
     */
    private String certPath;

    /**
     * 签名方式
     */
    @Value("${sign-type:HMAC-SHA256}")
    private String signType;
}
