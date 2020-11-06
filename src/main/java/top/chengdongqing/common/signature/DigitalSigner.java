package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.transform.SignBytes;

/**
 * 数字签名器
 * 统一入口
 *
 * @author Luyao
 */
public class DigitalSigner {

    /**
     * 执行签名
     *
     * @param content   签名内容
     * @param key       签名密钥
     * @param algorithm 签名算法
     * @return 数字签名
     */
    public static SignBytes signature(String content, byte[] key, SignatureAlgorithm algorithm) {
        return getInstance(algorithm).signature(content, key, algorithm);
    }

    /**
     * 验证签名
     *
     * @param content   验签内容
     * @param key       验签密钥
     * @param algorithm 签名算法
     * @param sign      待验证的签名
     * @return 验证结果
     */
    public static boolean verify(String content, byte[] key, SignatureAlgorithm algorithm, byte[] sign) {
        return getInstance(algorithm).verify(content, key, algorithm, sign);
    }

    /**
     * 获取签名器实例
     *
     * @param algorithm 签名算法
     * @return 签名器实例
     */
    private static IDigitalSigner getInstance(SignatureAlgorithm algorithm) {
        try {
            return algorithm.getSigner().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
