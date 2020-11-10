package top.chengdongqing.common.encrypt;

import top.chengdongqing.common.transformer.BytesToStr;

/**
 * 加解密器
 * 统一入口
 *
 * @author Luyao
 */
public class Encryptor {

    /**
     * 数据加密
     *
     * @param algorithm 加密算法
     * @param data      明文
     * @param key       密钥
     * @param password  口令
     * @return 密文
     */
    public static BytesToStr encrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password) {
        return getInstance(algorithm).encrypt(algorithm, data, key, password);
    }

    /**
     * 数据解密
     *
     * @param algorithm 加密算法
     * @param data      密文
     * @param key       密钥
     * @param password  口令
     * @return 明文
     */
    public static BytesToStr decrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password) {
        return getInstance(algorithm).decrypt(algorithm, data, key, password);
    }

    /**
     * 获取加解密器实例
     *
     * @param algorithm 加密算法
     * @return 加解密器实例
     */
    private static IEncryptor getInstance(EncryptAlgorithm algorithm) {
        try {
            return algorithm.getEncryptor().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
