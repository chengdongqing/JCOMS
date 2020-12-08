package top.chengdongqing.common.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

import java.security.SignatureException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * JWT处理器
 *
 * @author Luyao
 */
@Slf4j
@Component
public class JwtProcessorImpl implements JwtProcessor {

    @Autowired
    private JwtProps props;

    /**
     * 签名算法
     */
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.EdDSA_ED25519;

    @Override
    public JSONWebToken generate(Kv<String, Object> payloads) {
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
        String content = BytesToStr.of(header.toJson()).toURLBase64().concat(".") + BytesToStr.of(JsonKit.toJsonBytes(payloads)).toURLBase64();
        // 执行签名
        String signature = DigitalSigner.newInstance(ALGORITHM).signature(content,
                StrToBytes.of(props.getPrivateKey()).fromBase64())
                .toURLBase64();
        // 合成令牌
        String token = content.concat(".").concat(signature);
        // 返回token详情
        return JSONWebToken.builder()
                .token(token)
                .header(header)
                .payloads(payloads)
                .signature(signature)
                .build();
    }

    @Override
    public Kv<String, Object> parse(String token) throws SignatureException, TokenExpiredException {
        try {
            // 解析token
            JwtParser jwt = JwtParser.of(token);

            // 获取被签名的数据
            String content = jwt.getHeaders().rawStr().concat(".") + jwt.getPayloads().rawStr();
            // 验签
            boolean verified = DigitalSigner.newInstance(ALGORITHM).verify(content,
                    StrToBytes.of(jwt.sign()).fromURLBase64(),
                    StrToBytes.of(props.getPublicKey()).fromBase64());
            if (!verified) throw new SignatureException("token签名无效");

            // 判断是否过期
            if (Instant.ofEpochMilli(jwt.getHeaders().header().getExpiryTime()).isBefore(Instant.now())) {
                throw new TokenExpiredException("token已过期");
            }

            // 返回有效载荷
            return jwt.getPayloads().payloads();
        } catch (IllegalArgumentException e) {
            log.error("token异常", e);
            throw new IllegalArgumentException("token无效");
        }
    }
}

@Getter
@Setter
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
