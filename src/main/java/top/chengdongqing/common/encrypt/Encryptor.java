package top.chengdongqing.common.encrypt;

import top.chengdongqing.common.encrypt.encryptor.*;
import top.chengdongqing.common.transformer.BytesToStr;

/**
 * 加解密器
 *
 * @author Luyao
 * @see RSAEncryptor
 * @see AESCBCEncryptor
 * @see AESGCMEncryptor
 * @see AESPBEEncryptor
 */
public interface Encryptor {

    /**
     * 获取加解密器实例
     *
     * @param algorithm 加密算法
     * @return 加解密器
     */
    static Encryptor newInstance(EncryptAlgorithm algorithm) {
        try {
            return algorithm.getEncryptor().getDeclaredConstructor(EncryptAlgorithm.class).newInstance(algorithm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据加密
     *
     * @param data     明文
     * @param key      密钥
     * @param password 口令
     * @return 密文
     */
    BytesToStr encrypt(byte[] data, String key, String password);

    /**
     * 数据解密
     *
     * @param data     密文
     * @param key      密钥
     * @param password 口令
     * @return 明文
     */
    BytesToStr decrypt(byte[] data, String key, String password);
}
