package top.chengdongqing.common.payment.wxpay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置
 *
 * @author Luyao
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "payment.wx")
public class WxConstants {

    /**
     * 应用编号
     */
    private AppId appId;
    /**
     * 商户号
     */
    private String mchId;

    /**
     * 网站标题
     */
    @Value("${web-title}")
    private String webTitle;

    /**
     * 网站域名
     */
    @Value("${web-domain}")
    private String webDomain;

    @Data
    @Component
    @RefreshScope
    @ConfigurationProperties(prefix = "payment.wx.appid")
    public static class AppId {

        private String mp;
        private String pc;
        private String app;
        private String mb;
    }
}
