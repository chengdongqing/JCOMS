package top.chengdongqing.common.pay.wxpay;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付公共配置
 *
 * @author Luyao
 */
@Getter
@Setter
@Component
@ConfigurationProperties("pay.wx")
public class WxpayProps {

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
    private String wxDomain;

    @Getter
    @Setter
    @Component
    @ConfigurationProperties("pay.wx.app-id")
    public static class AppId {

        private String mp;
        private String pc;
        private String app;
        private String mb;
    }
}
