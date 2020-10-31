package top.chengdongqing.common.signature;

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
    public static Bytes signature(String content, SignatureAlgorithm algorithm) {
        return SignerHolder.SIGNER.signature(content, null, algorithm);
    }

    /**
     * 执行验签
     * 无需密钥
     */
    public static boolean validate(String content, SignatureAlgorithm algorithm, String sign) {
        return SignerHolder.SIGNER.validate(content, null, algorithm, new BigInteger(sign, 16).toByteArray());
    }

    @Override
    public Bytes signature(String content, byte[] key, SignatureAlgorithm algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            return Bytes.of(md.digest(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validate(String content, byte[] key, SignatureAlgorithm algorithm, byte[] sign) {
        return Arrays.equals(signature(content, key, algorithm).bytes(), sign);
    }
}
