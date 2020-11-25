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
     * 网关地址
     */
    @Value("${gateway:https://openapi.alipaydev.com/gateway.do}")
    private String gateway;
    /**
     * 应用编号
     */
    @Value("${app-id:2016081900286989}")
    private String appId;
    /**
     * 请求方法
     */
    @Autowired
    private AlipayMethod method;
    /**
     * 编码格式
     */
    @Value("utf-8")
    private String charset;
    /**
     * 签名类型
     */
    @Value("RSA2")
    private String signType;

    // 应用私钥
    @Value("${private-key:MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCgaRwZ01SBRUVP7kXUWK92jNgTv8QhbTNIxcaEvHO/M0BjUy8swiuZflLX3vckUTky/0nz+IROqrzb4Sk78GF329m8XBIEy+XmONi68sjxW/UptCp5gDVy9zZcuiv8j+OsjXBUHcvlGIC09MokYnYsA6FgxrR69GUz8DEUATmtzjdl1rZpLY96i21NsLNeluiHHlDXd13Wa7ofPA72Y/LlJa+V2TsPmxZJRIxXShlXu9QeNp/axu/S6zCv9Y1acXqGOVWykI255SRfikwSsgRvXWHrv65t7yO6029wj1mD8EMmeaFCaihxOQI0SVXCEkcuK1CSxtiIjQ07Gs9oJe/jAgMBAAECggEALkPinPg8yUQHZ7RbY5FomhKpR5jpopJyVizkwufvolfP3FXjp08hcBXlIunl8g2TJ8hFth4ZWu8XpXtdfzInioBYXmFXuRZt6ma+67yWhDZ3kV2rWnvF7rCqdzgi86c0jVIF8dzeL+dPR+HKY5AacTti0hy9dTED92TxYsmvrxb0EU8jx+FvYdfj/JEQsejcqk43FnoDbYgzbHX2iV1uECTuLy7LuTCXIgWK6xjmG3OycIzPK1QSXPyAKJ6UxUuTJHowpCWrjkcAS2UCW/KIU3Y4EiDuWCP0P5m+yCWNddezvLR6CoiVFoQzZIMqjwKe3Kf6PPm2SFzB4ug41OB7wQKBgQDUZmNl2XynMvu/+9CSK5AE8eir6dFIR2usik6amDXnFC/HZ8U8JaudVH1Cgfl59MNMK3QD7RDbbobqg2Z5ByVuIqm53yhquuSp16Z2KGxbZJIQVK/TM2A933F4l7cLWdJc/wAi5n6AV4ZE3RhC0mziwtfkU9SE/P8SXe59C5QDUQKBgQDBVq58NjuoSFRbzwDqQObIc+9HvUiNxveWxNNpDfm4vvYXlLKh7I8C0jTJ613FRtR6x9ZcAE2i1Csa2L3QGD4XteosOPO4b600iE5Dmtc4+6Uoys/Q68/f4HKy6gN8yiGremKfOqYu87tT06Y2CeCiKRYqtZazZ8IsYjkxDoOq8wKBgElVDG5mt432oUq4g1tkJPZAG0f3k2U0G23X1qmzcbDVnGVpY4SPsP/B4vlqbJr5sdADlKuiVtacA4LGWUPh/r9WwmjFLmwjGL23Eebrr/Wh9Hgk9QujSlAybDfpka4ele27sO1bzUM8dMQj3bN8eVGVWfqgItjDjMFnouaTb8fxAoGAE+Os845IuNkoBIHZIEAo/bofhYvNaPhtBA+fiBEt/Xl3q5lafxns5GdnktYpwV+2ACOKw9AsLHITNSHS9RNpX9sRCheZywGCGgNjucS2G2CX7wheNKntqqRWaF12mmHY8OEYQDUyX2YngUomsSx29Xewlhg0NqSX7yTzqT5LAU0CgYByoRghGan9L0wTSfAOw9385SXuDYI1RKkAFL9N1RUc7DZBqpmts9qBapHZuz/9CqBkutDJrvYLQzLuUaOACeB1XH9b+t5dPLl8eJsYfbSgIXEHQRsqYwWhLgYeh7xkLMjSvgrFt8nbRK+wBrMfYrtRLt/azAg8c1ew+fznVlLY2w==}")
    private String privateKey;
    // 应用公钥
    @Value("${public-key:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoGkcGdNUgUVFT+5F1FivdozYE7/EIW0zSMXGhLxzvzNAY1MvLMIrmX5S1973JFE5Mv9J8/iETqq82+EpO/Bhd9vZvFwSBMvl5jjYuvLI8Vv1KbQqeYA1cvc2XLor/I/jrI1wVB3L5RiAtPTKJGJ2LAOhYMa0evRlM/AxFAE5rc43Zda2aS2PeottTbCzXpbohx5Q13dd1mu6HzwO9mPy5SWvldk7D5sWSUSMV0oZV7vUHjaf2sbv0uswr/WNWnF6hjlVspCNueUkX4pMErIEb11h67+ube8jutNvcI9Zg/BDJnmhQmoocTkCNElVwhJHLitQksbYiI0NOxrPaCXv4wIDAQAB}")
    private String publicKey;
    // 支付宝根证书路径
    @Value("${alipay-root-cert-path:/certificates/alipayRootCert.crt}")
    private String alipayRootCertPath;
    // 支付宝公钥证书路径
    @Value("${alipay-cert-path:/certificates/alipayCert.crt}")
    private String alipayCertPath;
    // 应用公钥证书路径
    @Value("${app-cert-path:/certificates/appCert.crt}")
    private String appCertPath;
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
