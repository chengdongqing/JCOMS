package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.IDigitalSigner;
import top.chengdongqing.common.signature.transform.SignBytes;
import top.chengdongqing.common.signature.SignatureAlgorithm;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * 数字签名
 * 基于消息摘要算法
 *
 * @author Luyao
 */
public class MessageDigestSigner implements IDigitalSigner {

    @Override
    public SignBytes signature(String content, byte[] key, SignatureAlgorithm algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            return SignBytes.of(md.digest(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String content, byte[] key, SignatureAlgorithm algorithm, byte[] sign) {
        return Arrays.equals(signature(content, key, algorithm).bytes(), sign);
    }
}
