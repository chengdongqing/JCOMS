package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.signer.AsymmetricSigner;
import top.chengdongqing.common.signature.signer.HMacSigner;
import top.chengdongqing.common.signature.signer.MessageDigestSigner;
import top.chengdongqing.common.signature.transform.SignBytes;

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
     * @param content   签名的内容
     * @param key       密钥
     * @param algorithm 签名算法
     * @return 签名
     */
    SignBytes signature(String content, byte[] key, SignatureAlgorithm algorithm);

    /**
     * 验证签名是否有效
     *
     * @param content   签名的内容
     * @param key       密钥
     * @param algorithm 签名算法
     * @param sign      要验证的签名
     * @return 是否有效
     */
    boolean verify(String content, byte[] key, SignatureAlgorithm algorithm, byte[] sign);
}
