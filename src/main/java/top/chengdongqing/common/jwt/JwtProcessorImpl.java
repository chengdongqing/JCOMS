package top.chengdongqing.common.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
 * The implement of {@link JwtProcessor}
 *
 * @author Luyao
 */
@Slf4j
@Component
public class JwtProcessorImpl implements JwtProcessor {

    @Autowired
    private JwtProps props;

    /**
     * The signature algorithm
     */
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.EdDSA_ED25519;

    @Override
    public JSONWebToken generate(Kv<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            throw new IllegalArgumentException("The jwt payload cannot be empty.");
        }

        // header of the token
        JwtHeader header = new JwtHeader();
        header.setAlgorithm(ALGORITHM.getAlgorithm());
        // issue time of the token
        Instant now = Instant.now();
        header.setIssueTime(now.toEpochMilli());
        // expiry time of the token
        Instant expiryTime = now.plus(props.getEffectiveDuration(), ChronoUnit.MINUTES);
        header.setExpiryTime(expiryTime.toEpochMilli());
        // content for signature
        String content = BytesToStr.of(header.toJson()).toURLBase64().concat(".") + BytesToStr.of(JsonKit.toJsonBytes(payload)).toURLBase64();
        // executes signature
        String signature = DigitalSigner.newInstance(ALGORITHM).signature(content,
                StrToBytes.of(props.getPrivateKey()).fromBase64()).toURLBase64();
        // concatenates the header, the payload and the signature to be a complete token
        String token = content.concat(".").concat(signature);
        // returns the complete token entity
        return JSONWebToken.builder()
                .token(token)
                .header(header)
                .payload(payload)
                .signature(signature)
                .build();
    }

    @Override
    public Kv<String, Object> parse(String token) throws SignatureException, TokenExpiredException {
        try {
            // gets the parser of the token
            JwtParser jwt = JwtParser.of(token);

            // builds the content to verify signature
            String content = jwt.getHeader().rawStr().concat(".") + jwt.getPayload().rawStr();
            // verifies the token
            boolean verified = DigitalSigner.newInstance(ALGORITHM).verify(content,
                    StrToBytes.of(jwt.sign()).fromURLBase64(),
                    StrToBytes.of(props.getPublicKey()).fromBase64());
            if (!verified) throw new SignatureException("token签名无效");

            // determines the token was expired or not
            if (Instant.ofEpochMilli(jwt.getHeader().header().getExpiryTime()).isBefore(Instant.now())) {
                throw new TokenExpiredException("token已过期");
            }

            // returns the payload of the token;
            return jwt.getPayload().payload();
        } catch (IllegalArgumentException e) {
            log.error("token异常", e);
            throw new IllegalArgumentException("token无效");
        }
    }
}

@Getter
@Setter
@Component
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
