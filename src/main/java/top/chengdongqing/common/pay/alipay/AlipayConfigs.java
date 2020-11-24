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
    @Value("${gateway:https://openapi.alipaydev.com/gateway.do}")
    private String gateway;
    /**
     * 应用编号
     */
    @Value("${app-id:2016081900286989}")
    private String appId;
    /**
     * 支付业务请求方法名
     */
    @Autowired
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
     * 签名私钥
     */
    @Value("${private-key:MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCo9JWeFuw+SmZnjjbBZ+TNQTY6Ny7aYYPekFc8Mdo4+kbemvnOkcr30HX1NiTEpRCSgvJ+elZFm2yZLEwRfuwZjVtAxQ0tGoFyuPgrl+5Ah3rRg3MF6KdOMgtbqZF9pG5vslQ0pKVYHI8h8FqjNr5wlQiWtnLD07MwppWZJ3OaebqnCAkuwYxbPU3olocDRjpivQ7ZAD/iGWz/SJVcxp03j0X2zuau0ddmcurd60oF3pJwbL3ZnnPbwQNibUlocRM8U3TWbU3tmJWPEPLhRsQPPGXIzeqNkOXm08NiEjKwt9ASH7HgcvmBpI1AzgXqC93sv77qoSX4LIhxYXB8sve9AgMBAAECggEACSePf8xgKpPfbpb/EoWtmQ2Cga8Bw+VqvsF1aJaO621sL52YEYBGzhymd43ieiEzf6zGtOGuEQF15erHiF8nLc9B9YV3zEzWXL/2+m22BBsTJY0Iy3Yqsor1oJbUgklJjpXMCDovl1Gc08rxLkzGDtWUbI4gM8ohuG4eRvkjQ2hkPa5sBDPK2t9Q5A6g6pyQ2eM3g/z74qvxvjHZ98G6KWivgiWboRlRmCYetgYUNRS8p9tYUFRzNuKu6xa2QbnX5jaaMeZr127MYCoe52He0yvGh5pGpRmml9R+qH+gjd7Z+skdz9Vae6kVLyI9oNCdJYA1IQsTHCWJ19CipdQAhQKBgQD2tuPEGjL6R5YIJuTBNRHoaOxHFrw6RjeI1LQOe8P2Mxq9E/INEW1DhvLVleNnvzKXX31Vcg22xBo0wvxFCNEVe9/NH0Us1NcfXPXKnfXgIh2KB3OK92mwU9oC9ArhhPPEEONiPAWgWEk6DHr3GXPDlO0LcY6nOjtHSAeEOEFm4wKBgQCvUHs8Kv3OFPBRsnILgGHG8vY9QS8yQkiqRCFPwawTSlLfoBjGYyVf7s3z9t6z1kioC4c42Nbq2MtfQa0LjRuoGhk4zRm6vG5s046k8kzpCrUMY+9xS0zKoGDhctaULlT2nps2s7LlBQ2xV2cd4MwoCEeW6AH5kkefN3JmpZPI3wKBgEuWZxwa5sl1i0r+F5H2X4E0PEffeFXmU3dxq6TQWI6iw6twY/WLBE3vF5FGtgCAPNDR6cV3Byqa1Gib4MyJ0f1iEYR38yq0HyC1y3yDfuRnr2Mtu0G1YcUrO50yBe9oW4MTpYbgWtzwj6bgUgBI5QXkJ+gcp5cmqSjCscLZtXf/AoGAL1cbP/8X9CtBTb5S/3oWbSO0jIvqk7bEan3lROU2cysRv+M6bW/eas5QfP1/264UhpQ+WoJrXpVWLY3NjZ34BuetQeJYfU1ewDJMhYwMaq2CgCsIQF6mCN1P6/EXsoHkXYgsv5O8zg0USnwgkzfdyT2bH0vdJKh0gLD3SnKRup8CgYEAhgKlBetTwQl4ZRvbD3CpkDW5JfJr8SDnkP1lp42l04sBYOljqflxXqc7H/eLRttZ0AnS+CezAa2bUeEvkiO0PWHx7YsxX+RlZk+c6NprhKEv+pCEW9piwfPGX5hk1XdzmftBnymZWp+spW1RKT8eGrde+wdULqfm9daszzmMa4o=}")
    private String privateKey;
    /**
     * 验签公钥
     */
    @Value("${public-key:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6YtnH8lfW41r/84fRGxKT+Sn4LCXY7ki6cxUV8iIVuzgGBGlrW47mznkD03Xt02kUJcyKlQRByWTWc21XtT9KinvF51oYoQnnS9QRyGV0MBDx9CNcWVU3veEK1uRkILAHrBFl/sGTPWTBiLA2iNDEqHZO3yVik3Cx0gRYY2gObZ7tKxiplxQrBmXZCHC20Ew7PxfcO/mSAF58WMQidTtbZqhLm94akff+afXn0+kKt9JEwdRD3gnG30/DoETRjmSQo1HodGa7eYXeSG1sLCQ+SjiRUkGRfwxSIJ5UG857DhADZrw15PUlOZzz0zkFewglfoe5IGFqxJyDfc35LKR9wIDAQAB}")
    private String alipayPublicKey;
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
