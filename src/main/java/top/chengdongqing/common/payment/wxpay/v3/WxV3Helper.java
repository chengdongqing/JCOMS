package top.chengdongqing.common.payment.wxpay.v3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.wxpay.WxConstants;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * 微信支付V3工具类
 *
 * @author Luyao
 */
@Component
public class WxV3Helper {

    @Autowired
    private WxConstants constants;
    @Autowired
    private WxV3Constants v3Constants;

    /**
     * 获取包含签名的认证信息
     *
     * @param method  HTTP请求方式
     * @param apiPath 接口路径
     * @param body    请求体
     * @return 认证信息
     */
    public Kv<String, String> getAuthorization(HttpMethod method, String apiPath, String body) {
        // 时间戳
        String timestamp = getTimestamp();
        // 随机数
        String nonceStr = StrKit.getRandomUUID();
        // 数字签名
        String signature = WxV3Signer.signature(v3Constants.getPrivateKey(), method.name(), apiPath, body, timestamp, nonceStr);
        // 商户号
        String mchId = constants.getMchId();
        // 证书序列号
        String serialNo = v3Constants.getCertSerialNo();
        // 构建签名信息
        Kv<String, String> signInfo = Kv.go("mchid", mchId)
                .add("nonce_str", nonceStr)
                .add("timestamp", timestamp)
                .add("serial_no", serialNo)
                .add("signature", signature);
        String token = signInfo.entrySet().stream().map(item -> item.getKey()
                .concat("=\"")
                .concat(item.getValue())
                .concat("\""))
                .collect(Collectors.joining(","));
        return Kv.go("Authorization", v3Constants.getAuthSchema().concat(" ") + token);
    }

    /**
     * 获取域名后的接口路径
     *
     * @param path 业务路径
     * @return 包含前缀的接口路径
     */
    public static String getTradeApi(String path) {
        return "/v3/pay/transactions".concat(path);
    }

    /**
     * 获取完整请求地址
     *
     * @param path   请求路径
     * @return 完整请求地址
     */
    public String getRequestUrl(String path) {
        return constants.getWxDomain().concat(getTradeApi(path));
    }

    /**
     * 获取当前时间戳，精确到秒
     *
     * @return 时间戳
     */
    public static String getTimestamp() {
        return System.currentTimeMillis() / 1000 + "";
    }

    /**
     * 获取过期时间
     * @param duration 下单后允许付款时长，单位：分钟
     * @return 指定格式的过期时间字符串
     */
    public static String getExpireTime(long duration) {
        return LocalDateTime.now().plusMinutes(duration)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                // 去除秒后面的时间信息
                .replaceAll("\\.\\d+", "");
    }
}
