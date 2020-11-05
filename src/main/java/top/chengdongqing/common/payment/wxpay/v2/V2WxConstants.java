package top.chengdongqing.common.payment.wxpay.v2;

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
public class V2WxConstants {

    /**
     * 应用编号
     */
    private AppId appId;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 签名用的密钥
     */
    private String secretKey;

    /**
     * 订单请求支付地址
     */
    private String paymentUrl;
    /**
     * 支付成功通知地址
     */
    private String notifyUrl;
    /**
     * 关闭订单请求地址
     */
    private String closeUrl;
    /**
     * 订单退款请求地址
     */
    private String refundUrl;

    /**
     * 证书地址
     */
    private String certPath;

    /**
     * 签名方式
     */
    @Value("${sign-type:HMAC-SHA256}")
    private String signType;

    /**
     * 网站标题
     */
    @Value("${web-title}")
    private String webTitle;

    /**
     * 网站域名
     */
    @Value("${web-url}")
    private String webUrl;

    @Data
    @Component
    @RefreshScope
    @ConfigurationProperties(prefix = "payment.wx.v2.appid")
    public static class AppId {

        private String mp;
        private String pc;
        private String app;
        private String mb;
    }
}
