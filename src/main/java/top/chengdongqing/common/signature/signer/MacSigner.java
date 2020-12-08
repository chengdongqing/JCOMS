package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * 数字签名
 * 基于带消息认证码的消息摘要算法
 *
 * @author Luyao
 */
public record MacSigner(SignatureAlgorithm algorithm) implements DigitalSigner {

    @Override
    public BytesToStr signature(String content, byte[] key) {
        try {
            Mac mac = Mac.getInstance(algorithm.getAlgorithm());
            mac.init(new SecretKeySpec(key, algorithm.getAlgorithm()));
            return BytesToStr.of(mac.doFinal(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String content, byte[] sign, byte[] key) {
        return Arrays.equals(signature(content, key).bytes(), sign);
    }
}
