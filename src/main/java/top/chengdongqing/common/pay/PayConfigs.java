package top.chengdongqing.common.pay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付相关公共配置
 *
 * @author Luyao
 */
@Getter
@Component
@ConfigurationProperties("pay")
public class PayConfigs {

    /**
     * 支付超时配置，单位：分钟
     */
    @Value("${pay.timeout:30}")
    private Long timeout;

    /**
     * 网站域名
     */
    @Value("${pay.web-domain:https://www.chengdongqing.top}")
    private String webDomain;
}
