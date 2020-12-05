package top.chengdongqing.common.pay.alipay;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 支付宝支付公共配置
 *
 * @author Luyao
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("pay.ali")
public class AlipayProps {

    /**
     * 网关地址
     */
    private String gateway;
    /**
     * 应用编号
     */
    private String appId;
    /**
     * 请求方法
     */
    @Autowired
    private AlipayMethod method;
    /**
     * 编码格式
     */
    private String charset;
    /**
     * 签名类型
     */
    private String signType;

    // 应用私钥
    private String privateKey;
    // 应用公钥
    private String publicKey;
    // 支付宝根证书路径
    private String alipayRootCertPath;
    // 支付宝公钥证书路径
    private String alipayPublicKeyCertPath;
    // 应用公钥证书路径
    private String appPublicKeyCertPath;

    /**
     * 接口版本
     */
    private String version;
    /**
     * 支付通知回调地址
     */
    private String notifyUrl;

    @Getter
    @Component
    public static class AlipayMethod {

        // 请求付款方法
        @Autowired
        private AlipayTradeMethod pay;
        // 关闭订单方法
        @Value("alipay.trade.close")
        private String close;
        // 查询订单方法
        @Value("alipay.trade.query")
        private String query;
        // 请求退款方法
        @Value("alipay.trade.refund")
        private String refund;

        @Getter
        @Component
        public static class AlipayTradeMethod {

            @Value("alipay.trade.create")
            private String create;
            @Value("alipay.trade.app.pay")
            private String appPay;
            @Value("alipay.trade.wap.pay")
            private String wapPay;
            @Value("alipay.trade.page.pay")
            private String pagePay;
            @Value("alipay.trade.precreate")
            private String preCreate;
        }
    }
}
