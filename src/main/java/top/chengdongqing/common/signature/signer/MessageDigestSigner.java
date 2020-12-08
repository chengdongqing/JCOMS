package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * 数字签名
 * 基于消息摘要算法
 *
 * @author Luyao
 */
public record MessageDigestSigner(SignatureAlgorithm algorithm) implements DigitalSigner {

    @Override
    public BytesToStr signature(String content, byte[] key) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            return BytesToStr.of(md.digest(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String content, byte[] sign, byte[] key) {
        return Arrays.equals(signature(content, key).bytes(), sign);
    }
}
