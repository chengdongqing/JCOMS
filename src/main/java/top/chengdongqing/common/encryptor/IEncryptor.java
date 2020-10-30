package top.chengdongqing.common.encryptor;

/**
 * 加解密器
 *
 * @author Luyao
 */
public interface IEncryptor {

    /**
     * 对称加密
     *
     * @param data 明文
     * @param key  密钥
     * @return 密文
     */
    byte[] encrypt(byte[] data, byte[] key);

    /**
     * 对称解密
     *
     * @param data 密文
     * @param key  密钥
     * @return 明文
     */
    byte[] decrypt(byte[] data, byte[] key);

    /**
     * 基于口令加密
     * @param data 明文
     * @param key 密钥
     * @param password 口令
     * @return 密文
     */
    byte[] encrypt(byte[] data, byte[] key, String password);

    /**
     * 基于口令解密
     *
     * @param data 密文
     * @param key  密钥
     * @param password 口令
     * @return 明文
     */
    byte[] decrypt(byte[] data, byte[] key, String password);
}
