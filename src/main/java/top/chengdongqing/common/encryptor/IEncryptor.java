package top.chengdongqing.common.encryptor;

import top.chengdongqing.common.encryptor.entity.DecryptEntity;
import top.chengdongqing.common.encryptor.entity.EncryptEntity;

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
     * @param entity 参数实体
     * @return 密文
     */
    DecryptEntity encrypt(EncryptEntity entity);

    /**
     * 对称解密
     *
     * @param entity 参数实体
     * @return 明文
     */
    byte[] decrypt(EncryptEntity entity);
}
