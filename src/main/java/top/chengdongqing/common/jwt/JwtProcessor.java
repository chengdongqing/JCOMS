package top.chengdongqing.common.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.transformer.StrToBytes;

import java.security.SignatureException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * JWT处理器
 *
 * @author Luyao
 */
@Component
public class JwtProcessor implements IJwtProcessor {

    @Autowired
    private JwtProps props;

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
        Instant expiryTime = now.plus(props.getEffectiveDuration(), ChronoUnit.MINUTES);
        header.setExpiryTime(expiryTime.toEpochMilli());
        // 拼接待签名内容
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String content = encoder.encodeToString(header.toJson()).concat(".") + encoder.encodeToString(JsonKit.toJsonBytes(payloads));
        // 执行签名
        String signature = DigitalSigner.signature(ALGORITHM, content,
                StrToBytes.of(props.getPrivateKey()).fromBase64())
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
        try {
            // 解析token
            JwtParser jwt = JwtParser.of(token);

            // 获取被签名的数据
            String content = jwt.getHeaders().rawStr().concat(".") + jwt.getPayloads().rawStr();
            // 验签
            boolean verified = DigitalSigner.verify(ALGORITHM, content,
                    StrToBytes.of(props.getPublicKey()).fromBase64(),
                    StrToBytes.of(jwt.sign()).fromBase64());
            if (!verified) throw new SignatureException("token签名无效");

            // 判断是否过期
            if (Instant.ofEpochMilli(jwt.getHeaders().header().getExpiryTime()).isBefore(Instant.now())) {
                throw new TokenExpiredException("token已过期");
            }

            // 返回有效载荷
            return jwt.getPayloads().payloads();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("token无效");
        }
    }
}

@Data
@Component
@RefreshScope
@ConfigurationProperties("jwt")
class JwtProps {

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
