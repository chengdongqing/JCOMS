package top.chengdongqing.common.payment.wxpay.v3;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.encrypt.EncryptAlgorithm;
import top.chengdongqing.common.encrypt.Encryptor;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;
import top.chengdongqing.common.payment.wxpay.WxStatus;
import top.chengdongqing.common.payment.wxpay.v3.callback.entity.EncryptResource;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * 微信支付V3工具类
 *
 * @author Luyao
 */
@Slf4j
@Component
public class WxV3Helper {

    @Autowired
    private WxConstants constants;
    @Autowired
    private WxV3Constants v3Constants;

    /**
     * 构建包含认证信息的请求头
     *
     * @param method  HTTP请求方式
     * @param apiPath 接口路径
     * @param body    请求体
     * @return 认证信息
     */
    public Kv<String, String> buildHeaders(HttpMethod method, String apiPath, String body) {
        // 时间戳
        String timestamp = WxPayHelper.getTimestamp();
        // 随机数
        String nonceStr = StrKit.getRandomUUID();
        // 数字签名
        String signature = signature(v3Constants.getPrivateKey(), method.name(), apiPath, body, timestamp, nonceStr);
        // 商户号
        String mchId = constants.getMchId();
        // 证书序列号
        String serialNo = v3Constants.getCertSerialNo();
        // 构建签名信息
        String token = Kv.go("mchid", mchId)
                .add("nonce_str", nonceStr)
                .add("timestamp", timestamp)
                .add("serial_no", serialNo)
                .add("signature", signature)
                .entrySet().stream().map(item -> item.getKey()
                        .concat("=\"")
                        .concat(item.getValue())
                        .concat("\""))
                .collect(Collectors.joining(","));
        return HttpKit.buildJSONHeaders().add("Authorization", v3Constants.getAuthSchema().concat(" ").concat(token));
    }

    /**
     * 获取域名后的接口路径
     *
     * @param path 业务路径
     * @return 包含前缀的接口路径
     */
    public static String buildTradeApi(String path) {
        return "/v3/pay/transactions".concat(path);
    }

    /**
     * 获取完整请求地址
     *
     * @param path 请求路径
     * @return 完整请求地址
     */
    public String buildRequestUrl(String path) {
        return constants.getWxDomain().concat(path);
    }

    /**
     * 生成签名
     * 签名算法：SHA256-RSA2048
     *
     * @param key    私钥
     * @param params 参数
     * @return 数字签名
     */
    public String signature(String key, String... params) {
        // 获取待签名字符串
        String content = getContent(params);
        // 生成签名
        return DigitalSigner.signature(SignatureAlgorithm.RSA_SHA256,
                content,
                StrToBytes.of(key).fromBase64())
                .toBase64();
    }

    /**
     * 验证签名
     *
     * @param serialNo 证书序列号
     * @param key      公钥
     * @param sign     签名
     * @param params   参数数组
     * @return 验证结果
     */
    public boolean verify(String serialNo, String key, String sign, String... params) {
        // 验证序列号
        // TODO

        // 获取待签名字符串
        String content = getContent(params);
        // 验证签名
        return DigitalSigner.verify(SignatureAlgorithm.RSA_SHA256,
                content,
                StrToBytes.of(key).fromBase64(),
                StrToBytes.of(sign).fromBase64());
    }

    /**
     * 获取待签名字符串
     * 主要是每个参数后加一个换行符，包括最后一行
     *
     * @param params 待签名字符数组
     * @return 待签名字符串
     */
    private static String getContent(String... params) {
        if (StringUtils.isAnyBlank(params)) {
            throw new IllegalArgumentException("The params can not be null or empty.");
        }
        // 换行符
        String linefeed = "\n";
        return String.join(linefeed, params).concat(linefeed);
    }

    /**
     * 构建成功响应信息
     *
     * @return 成功JSON字符串
     */
    public static String buildSuccessMsg() {
        return Kv.go("code", WxStatus.SUCCESS).toJson();
    }

    /**
     * 构建失败响应信息
     *
     * @param errorMsg 错误信息
     * @return 失败JSON字符串
     */
    public static Ret buildFailRes(String errorMsg) {
        return Ret.fail(Kv.go("code", WxStatus.FAIL).add("message", errorMsg).toJson());
    }

    /**
     * 解密微信回调数据
     *
     * @param body      包含加密数据的回调原始数据
     * @param secretKey 解密密钥
     * @return 解密后的核心数据对象
     */
    public static <T> T decryptData(String body, String secretKey, Class<T> clazz) {
        String encryptionInfoJson = JSON.parseObject(body).getString("resource");
        EncryptResource encryptResource = JSON.parseObject(encryptionInfoJson, EncryptResource.class);
        // 获取密文、随机数、关联数据
        byte[] ciphertext = StrToBytes.of(encryptResource.getCiphertext()).fromBase64();
        byte[] iv = encryptResource.getNonce().getBytes();
        String associatedData = encryptResource.getAssociatedData();
        // 解密数据
        String resourceJson = Encryptor.decrypt(EncryptAlgorithm.AES_GCM_NoPadding,
                ByteUtils.concatenate(iv, ciphertext),
                secretKey, associatedData)
                .toText();
        return JSON.parseObject(resourceJson, clazz);
    }

    /**
     * 转换时间
     *
     * @param time 时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime convertTime(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
