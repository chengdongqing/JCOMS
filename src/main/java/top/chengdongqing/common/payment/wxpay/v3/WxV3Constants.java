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
    @Value("${close-url:https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s/close}")
    private String closeUrl;
    /**
     * 订单退款请求地址
     */
    @Value("${refund-url:https://api.mch.weixin.qq.com/v3/ecommerce/refunds/apply}")
    private String refundUrl;

    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
}
