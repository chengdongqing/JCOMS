package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.signer.AsymmetricSigner;
import top.chengdongqing.common.signature.signer.HMacSigner;
import top.chengdongqing.common.signature.signer.MessageDigestSigner;
import top.chengdongqing.common.transformer.BytesToStr;

/**
 * 数字签名器顶层接口
 *
 * @author Luyao
 * @see AsymmetricSigner
 * @see HMacSigner
 * @see MessageDigestSigner
 */
public interface IDigitalSigner {

    /**
     * 执行数字签名
     *
     * @param algorithm 签名算法
     * @param content   签名的内容
     * @param key       密钥
     * @return 签名
     */
    BytesToStr signature(SignatureAlgorithm algorithm, String content, byte[] key);

    /**
     * 验证签名是否有效
     *
     * @param algorithm 签名算法
     * @param content   签名的内容
     * @param key       密钥
     * @param sign      要验证的签名
     * @return 是否有效
     */
    boolean verify(SignatureAlgorithm algorithm, String content, byte[] key, byte[] sign);
}
