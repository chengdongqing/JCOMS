package top.chengdongqing.common.signature.digest;

import top.chengdongqing.common.signature.ByteArray;
import top.chengdongqing.common.signature.IDigitalSigner;
import top.chengdongqing.common.signature.SignAlgorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * 数字签名
 * 基于消息摘要算法
 *
 * @author Luyao
 */
public class MessageDigestSigner implements IDigitalSigner {

    private static class SignerHolder {
        private static final MessageDigestSigner SIGNER = new MessageDigestSigner();
    }

    /**
     * 执行签名
     * 无需密钥
     */
    public static ByteArray signature(String content, SignAlgorithm algorithm) {
        return SignerHolder.SIGNER.signature(content, null, algorithm);
    }

    /**
     * 执行验签
     * 无需密钥
     */
    public static boolean validate(String content, SignAlgorithm algorithm, String sign) {
        return SignerHolder.SIGNER.validate(content, null, algorithm, new BigInteger(sign, 16).toByteArray());
    }

    @Override
    public ByteArray signature(String content, byte[] key, SignAlgorithm algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            return ByteArray.of(md.digest(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validate(String content, byte[] key, SignAlgorithm algorithm, byte[] sign) {
        return Arrays.equals(signature(content, key, algorithm).bytes(), sign);
    }
}
