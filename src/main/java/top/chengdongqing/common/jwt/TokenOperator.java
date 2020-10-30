package top.chengdongqing.common.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.signature.SignAlgorithm;
import top.chengdongqing.common.signature.asymmetric.AsymmetricSigner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * token生成与验证工具
 *
 * @author Luyao
 */
@Component
public class TokenOperator {

    @Autowired
    private Constants constants;

    /**
     * 生成token
     * token结构：base64(headers).base64(payloads).base64(signature)
     * 待签名内容：signature(base64(headers.toJson()).base64(payloads.toJson()))
     *
     * @param payloads 有效载荷
     * @return token对象
     */
    public Token generate(JSONObject payloads) {
        if (payloads == null || payloads.isEmpty()) throw new IllegalArgumentException("The payloads cannot be empty.");

        // 头部
        JSONObject headers = new JSONObject();
        // 签名算法
        headers.put("algorithm", SignAlgorithm.EdDSA_ED25519.getAlgorithm());
        Instant now = Instant.now();
        // 签发时间
        headers.put("issueTime", now.toEpochMilli() + "");
        Instant expiryTime = now.plus(constants.getDuration(), ChronoUnit.MINUTES);
        // 过期时间
        headers.put("expiryTime", expiryTime.toEpochMilli() + "");
        // 拼接待签名内容
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String content = encoder.encodeToString(JSON.toJSONBytes(headers)) + "." + encoder.encodeToString(JSON.toJSONBytes(payloads));
        // 执行签名
        String signature = AsymmetricSigner.signature(content, constants.getPrivateKey(), SignAlgorithm.EdDSA_ED25519).toBase64();
        content += "." + signature;
        return new Token(content, LocalDateTime.ofInstant(expiryTime, ZoneId.systemDefault()));
    }

    /**
     * 验证token是否有效
     *
     * @param token token
     * @return 是否有效
     */
    public boolean validate(String token) {
        if (StringUtils.isBlank(token)) throw new IllegalArgumentException("The token cannot be blank");
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("The token is wrong.");

        // 获取被签名的数据
        String content = parts[0] + "." + parts[1];

        // 验签
        boolean validate = AsymmetricSigner.validate(content, constants.getPublicKey(), SignAlgorithm.EdDSA_ED25519, parts[2]);
        if (!validate) return false;

        // 判断是否过期
        return Instant.ofEpochMilli(Long.parseLong(toMap(parts[0]).getString("expiryTime"))).isAfter(Instant.now());
    }

    /**
     * 从token中获取有效载荷
     *
     * @param token token
     * @return 有效载荷
     */
    public JSONObject getPayloads(String token) {
        String[] parts = getParts(token);
        return toMap(parts[1]);
    }

    /**
     * 将token根据.分成3部分
     */
    private String[] getParts(String token) {
        if (StringUtils.isBlank(token)) throw new IllegalArgumentException("The token cannot be blank");
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("The token is wrong.");
        return parts;
    }

    /**
     * 将token某一段内容解码并转map
     */
    private JSONObject toMap(String content) {
        JSONObject object = JSON.parseObject(new String(Base64.getUrlDecoder().decode(content)));
        if (object == null || object.isEmpty()) throw new IllegalStateException("token解析失败");
        return object;
    }
}

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "jwt")
class Constants {

    /**
     * 公钥，验签用
     */
    private String publicKey;
    /**
     * 私钥，签名用
     */
    private String privateKey;
    /**
     * 有效时长，单位：分钟
     */
    private Long duration;
}
