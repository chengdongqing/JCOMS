package top.chengdongqing.common.encryptor;

/**
 * 加解密器
 *
 * @author Luyao
 * @see AESEncryptor
 * @see RSAEncryptor
 */
public interface IEncryptor {

    /**
     * 对称加密
     *
     * @param data 明文
     * @param key  密钥
     * @return 密文
     */
    byte[] encrypt(byte[] data, String key);

    /**
     * 对称解密
     *
     * @param data 密文
     * @param key  密钥
     * @return 明文
     */
    byte[] decrypt(byte[] data, String key);

    /**
     * 基于口令加密
     *
     * @param data     明文
     * @param key      密钥
     * @param password 口令
     * @return 密文
     */
    default byte[] encrypt(byte[] data, String key, String password) {
        return null;
    }

    /**
     * 基于口令解密
     *
     * @param data     密文
     * @param key      密钥
     * @param password 口令
     * @return 明文
     */
    default byte[] decrypt(byte[] data, String key, String password) {
        return null;
    }
}
