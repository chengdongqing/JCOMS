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
            return signature(algorithm, content.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String content, byte[] sign, byte[] key) {
        return Arrays.equals(signature(content, key).bytes(), sign);
    }

    /**
     * Executes signature, usually for file
     *
     * @param algorithm the signature algorithm, like MD5, SHA-1, SHA-256...
     * @param data      the data to signature
     * @return the digital signature
     */
    public static BytesToStr signature(SignatureAlgorithm algorithm, byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            return BytesToStr.of(md.digest(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
