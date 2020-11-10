package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.IDigitalSigner;
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
public class MessageDigestSigner implements IDigitalSigner {

    @Override
    public BytesToStr signature(SignatureAlgorithm algorithm, String content, byte[] key) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            return BytesToStr.of(md.digest(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(SignatureAlgorithm algorithm, String content, byte[] key, byte[] sign) {
        return Arrays.equals(signature(algorithm, content, key).bytes(), sign);
    }
}
