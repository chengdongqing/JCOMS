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
public class V3Constants {

    /**
     * 私钥
     */
    @Value("${v3.private-key}")
    private String privateKey;
    /**
     * 公钥
     */
    @Value("${v3.public-key}")
    private String publicKey;
}
