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
     * 下单后允许付款时长，单位：分钟
     */
    @Value("${pay-duration:30}")
    private Long payDuration;

    /**
     * 微信支付域名
     */
    @Value("${wx-domain:https://api.mch.weixin.qq.com}")
    private String wxDomain;

    /**
     * 网站标题
     */
    private String webTitle;

    /**
     * 网站域名
     */
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
