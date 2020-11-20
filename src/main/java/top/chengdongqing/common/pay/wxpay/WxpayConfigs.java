package top.chengdongqing.common.pay.wxpay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 微信支付公共配置
 *
 * @author Luyao
 */
@Getter
@Component
@RefreshScope
@ConfigurationProperties("pay.wx")
public class WxpayConfigs {

    /**
     * 应用编号
     */
    @Autowired
    private AppId appId;
    /**
     * 商户号
     */
    private String mchId;

    /**
     * 微信支付域名
     */
    @Value("${wx-domain:https://api.mch.weixin.qq.com}")
    private String wxDomain;

    @Getter
    @Component
    @RefreshScope
    @ConfigurationProperties("pay.wx.appid")
    public static class AppId {

        private String mp;
        private String pc;
        private String app;
        private String mb;
    }
}
