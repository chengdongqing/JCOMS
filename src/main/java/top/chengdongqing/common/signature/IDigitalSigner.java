package top.chengdongqing.common.signature;

/**
 * 数字签名器顶层接口
 *
 * @author Luyao
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
    ByteArray signature(String content, byte[] key, SignAlgorithm algorithm);

    /**
     * 验证签名是否有效
     *
     * @param content   签名的内容
     * @param key       密钥
     * @param algorithm 签名算法
     * @param sign      要验证的签名
     * @return 是否有效
     */
    boolean validate(String content, byte[] key, SignAlgorithm algorithm, byte[] sign);
}