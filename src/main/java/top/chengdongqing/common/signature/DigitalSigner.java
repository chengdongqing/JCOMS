package top.chengdongqing.common.signature;

import top.chengdongqing.common.transformer.BytesToStr;

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
     * @param algorithm 签名算法
     * @param content   签名内容
     * @param key       签名密钥
     * @return 数字签名
     */
    public static BytesToStr signature(SignatureAlgorithm algorithm, String content, byte[] key) {
        return getInstance(algorithm).signature(algorithm, content, key);
    }

    /**
     * 验证签名
     *
     * @param algorithm 签名算法
     * @param content   验签内容
     * @param key       验签密钥
     * @param sign      待验证的签名
     * @return 验证结果
     */
    public static boolean verify(SignatureAlgorithm algorithm, String content, byte[] key, byte[] sign) {
        return getInstance(algorithm).verify(algorithm, content, key, sign);
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
