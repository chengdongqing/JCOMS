package top.chengdongqing.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.security.SignatureException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * JWT处理器
 * 基于EdDSA数字签名算法
 *
 * @author Luyao
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtProcessor implements IJwtProcessor {

    /**
     * 签名算法
     */
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.EdDSA_ED25519;

    @Autowired
    private JwtConfigs configs;

    @Override
    public JsonWebToken generate(Kv<String, Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            throw new IllegalArgumentException("The jwt payloads cannot be empty.");
        }

        // 头部信息
        JwtHeader header = new JwtHeader();
        // 签名算法
        header.setAlgorithm(ALGORITHM.getAlgorithm());
        // 签发时间
        Instant now = Instant.now();
        header.setIssueTime(now.toEpochMilli());
        // 过期时间
        Instant expiryTime = now.plus(configs.getEffectiveDuration(), ChronoUnit.MINUTES);
        header.setExpiryTime(expiryTime.toEpochMilli());
        // 拼接待签名内容
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String content = encoder.encodeToString(header.toJson()).concat(".") + encoder.encodeToString(JsonKit.toJsonBytes(payloads));
        // 执行签名
        String signature = DigitalSigner.signature(ALGORITHM, content,
                StrToBytes.of(configs.getPrivateKey()).fromBase64())
                .toBase64();
        // 合成令牌
        String token = content.concat(".").concat(signature);
        // 返回token详情
        return JsonWebToken.builder()
                .token(token)
                .header(header)
                .payloads(payloads)
                .signature(signature)
                .build();
    }

    @Override
    public Kv<String, Object> verify(String token) throws SignatureException, TokenExpiredException {
        String[] parts;
        try {
            // 获取token的每部分
            parts = getParts(token);

            // 获取被签名的数据
            String content = parts[0].concat(".").concat(parts[1]);
            // 验签
            boolean verified = DigitalSigner.verify(ALGORITHM, content,
                    StrToBytes.of(configs.getPublicKey()).fromBase64(),
                    StrToBytes.of(parts[2]).fromBase64());
            if (!verified) throw new SignatureException("token签名无效");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("token无效");
        }

        // 解码头部信息
        Base64.Decoder decoder = Base64.getUrlDecoder();
        JwtHeader header = JsonKit.parseObject(decoder.decode(parts[0]), JwtHeader.class);

        // 判断是否过期
        if (Instant.ofEpochMilli(header.getExpiryTime()).isBefore(Instant.now())) {
            throw new TokenExpiredException("token已过期");
        }

        // 解码有效载荷
        Kv<String, Object> payloads = JsonKit.parseKv(decoder.decode(parts[1]));
        if (payloads == null || payloads.isEmpty()) {
            throw new IllegalStateException("not any payloads of token " + token);
        }
        return payloads;
    }

    /**
     * 将token根据点分成3部分
     *
     * @param token 令牌
     * @return token的每部分
     */
    private String[] getParts(String token) {
        if (StringUtils.isBlank(token)) throw new IllegalArgumentException();
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException();
        return parts;
    }
}

@Data
@Component
@RefreshScope
@ConfigurationProperties("jwt")
class JwtConfigs {

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
    private Long effectiveDuration;
}

@Data
class JwtHeader {

    /**
     * 签名算法
     */
    private String algorithm;
    /**
     * 签发时间
     */
    private Long issueTime;
    /**
     * 过期时间
     */
    private Long expiryTime;

    /**
     * 转JSON字节数组
     *
     * @return 当前对象的JSON字节数组
     */
    public byte[] toJson() {
        return JsonKit.toJsonBytes(this);
    }
}