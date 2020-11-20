package top.chengdongqing.common.pay.alipay;

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
@Getter
@Component
@RefreshScope
@ConfigurationProperties("pay.ali")
public class AlipayConfigs {

    /**
     * 支付宝网关地址
     */
    @Value("${ali-gateway:https://openapi.alipay.com/gateway.do}")
    private String aliGateway;
    /**
     * 应用编号
     */
    private String appId;
    /**
     * 支付业务请求方法名
     */
    private AlipayMethod method;
    /**
     * 编码格式
     */
    @Value("utf-8")
    private String charset;
    /**
     * 签名算法类型
     */
    @Value("RSA2")
    private String signType;
    /**
     * 接口版本
     */
    @Value("1.0")
    private String version;
    /**
     * 支付通知回调地址
     */
    private String notifyUrl;

    @Getter
    @Component
    @ConfigurationProperties("alipay.trade.")
    public static class AlipayMethod {

        // 请求付款方法
        @Autowired
        private AlipayTradeMethod pay;
        // 关闭订单方法
        @Value("close")
        private String close;
        // 查询订单方法
        @Value("query")
        private String query;
        // 请求退款方法
        @Value("refund")
        private String refund;

        @Getter
        @Component
        @ConfigurationProperties("alipay.trade.")
        public static class AlipayTradeMethod {

            @Value("create")
            private String unifiedPay;
            @Value("app.pay")
            private String appPay;
            @Value("wap.pay")
            private String mbPay;
            @Value("precreate")
            private String pcPay;
        }
    }
}
