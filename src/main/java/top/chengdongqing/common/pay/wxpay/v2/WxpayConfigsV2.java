package top.chengdongqing.common.pay.wxpay.v2;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置
 * v2
 *
 * @author Luyao
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("pay.wx.v2")
public class WxpayConfigsV2 {

    /**
     * 微信接口请求地址
     */
    @Autowired
    private WxpayRequestApiV2 requestApi;
    /**
     * 签名的key
     */
    private String key;
    /**
     * 签名方式
     */
    private String signType;
    /**
     * 请求退款需要的双向证书路径
     */
    private String certPath;
    /**
     * 支付通知回调地址
     */
    private String notifyUrl;

    @Getter
    @Component
    public static class WxpayRequestApiV2 {

        // 请求付款接口
        @Value("/pay/unifiedorder")
        private String pay;
        // 关闭订单接口
        @Value("/pay/closeorder")
        private String close;
        // 请求退款接口
        @Value("/secapi/pay/refund")
        private String refund;
        // 查询订单接口
        @Value("/pay/orderquery")
        private String query;
    }
}
