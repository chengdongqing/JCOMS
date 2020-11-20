package top.chengdongqing.common.pay.wxpay.v3;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.encrypt.EncryptAlgorithm;
import top.chengdongqing.common.encrypt.Encryptor;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.pay.wxpay.WxConfigs;
import top.chengdongqing.common.pay.wxpay.WxPayHelper;
import top.chengdongqing.common.pay.wxpay.WxStatus;
import top.chengdongqing.common.pay.wxpay.v3.callback.entities.EncryptedResource;
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
    private WxConfigs configs;
    @Autowired
    private WxV3Configs v3Configs;

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
        String signature = signature(v3Configs.getPrivateKey(), method.name(), apiPath, body, timestamp, nonceStr);
        // 商户号
        String mchId = configs.getMchId();
        // 证书序列号
        String serialNo = v3Configs.getCertSerialNo();
        // 构建签名信息
        String token = Kv.of("mchid", mchId)
                .add("nonce_str", nonceStr)
                .add("timestamp", timestamp)
                .add("serial_no", serialNo)
                .add("signature", signature)
                .entrySet().stream().map(item -> item.getKey()
                        .concat("=\"")
                        .concat(item.getValue())
                        .concat("\""))
                .collect(Collectors.joining(","));
        return HttpKit.buildJSONHeaders().add("Authorization", v3Configs.getAuthSchema().concat(" ").concat(token));
    }

    /**
     * 构建除域名外的交易接口
     *
     * @param path 业务路径
     * @return 包含前缀的交易接口地址
     */
    public static String buildTradeApi(String path) {
        return "/v3/pay/transactions".concat(path);
    }

    /**
     * 构建除域名外的交易接口
     *
     * @param path   业务路径
     * @param params 查询参数
     * @return 除域名外的完整交易接口地址
     */
    public static String buildTradeApi(String path, Kv<String, String> params) {
        return buildTradeApi(path).concat("?").concat(StrKit.buildQueryStr(params));
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
        // 构建待签名字符串
        String content = buildContent(params);
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

        // 构建待签名字符串
        String content = buildContent(params);
        // 验证签名
        return DigitalSigner.verify(SignatureAlgorithm.RSA_SHA256,
                content,
                StrToBytes.of(key).fromBase64(),
                StrToBytes.of(sign).fromBase64());
    }

    /**
     * 构建待签名字符串
     * 主要是每个参数后加一个换行符，包括最后一行
     *
     * @param params 待签名字符数组
     * @return 待签名字符串
     */
    private static String buildContent(String... params) {
        if (StringUtils.isAnyBlank(params)) {
            throw new IllegalArgumentException("The params can not be null or empty.");
        }
        // 换行符
        String linefeed = "\n";
        return String.join(linefeed, params).concat(linefeed);
    }

    /**
     * 构建回调成功响应信息
     *
     * @return 成功JSON字符串
     */
    public static String getSuccessCallback() {
        return Kv.of("code", WxStatus.SUCCESS).toJson();
    }

    /**
     * 构建回调失败响应信息
     *
     * @param errorMsg 错误信息
     * @return 失败JSON字符串
     */
    public static <T> Ret<T> buildFailCallback(String errorMsg) {
        return Ret.fail(Kv.of("code", WxStatus.FAIL).add("message", errorMsg).toJson());
    }

    /**
     * 解密微信回调数据
     *
     * @param body      包含加密数据的回调原始数据
     * @param secretKey 解密密钥
     * @return 解密后的核心数据对象
     */
    public static <T> T decryptData(String body, String secretKey, Class<T> clazz) {
        String encryptedJson = JsonKit.parseKv(body).getAs("resource");
        EncryptedResource encryptedResource = JsonKit.parseObject(encryptedJson, EncryptedResource.class);
        // 获取密文、随机数、关联数据
        byte[] ciphertext = StrToBytes.of(encryptedResource.getCiphertext()).fromBase64();
        byte[] iv = encryptedResource.getNonce().getBytes();
        String associatedData = encryptedResource.getAssociatedData();
        // 解密数据
        String resourceJson = Encryptor.decrypt(EncryptAlgorithm.AES_GCM_NoPadding,
                ByteUtils.concatenate(iv, ciphertext),
                secretKey, associatedData)
                .toText();
        return JsonKit.parseObject(resourceJson, clazz);
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
