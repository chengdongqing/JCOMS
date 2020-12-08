package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.signer.*;
import top.chengdongqing.common.transformer.BytesToStr;

/**
 * 数字签名器
 *
 * @author Luyao
 * @see AsymmetricSigner
 * @see MacSigner
 * @see MessageDigestSigner
 */
public interface DigitalSigner {

    /**
     * 获取数字签名器实例
     *
     * @param algorithm 签名算法
     * @return 数字签名器
     */
    static DigitalSigner newInstance(SignatureAlgorithm algorithm) {
        try {
            return algorithm.getSigner().getDeclaredConstructor(SignatureAlgorithm.class).newInstance(algorithm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行签名
     *
     * @param content 签名的内容
     * @param key     签名密钥
     * @return 数字签名
     */
    BytesToStr signature(String content, byte[] key);

    /**
     * 验证签名
     *
     * @param content 签名的内容
     * @param sign    要验证的签名
     * @param key     验签密钥
     * @return 是否有效
     */
    boolean verify(String content, byte[] sign, byte[] key);
}
