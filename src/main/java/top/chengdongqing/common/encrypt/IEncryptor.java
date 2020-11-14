package top.chengdongqing.common.encrypt;

import top.chengdongqing.common.encrypt.encryptor.AESCBCEncryptor;
import top.chengdongqing.common.encrypt.encryptor.AESGCMEncryptor;
import top.chengdongqing.common.encrypt.encryptor.AESPBEEncryptor;
import top.chengdongqing.common.encrypt.encryptor.RSAEncryptor;
import top.chengdongqing.common.transformer.BytesToStr;

/**
 * 加解密器顶层接口
 *
 * @author Luyao
 * @see RSAEncryptor
 * @see AESCBCEncryptor
 * @see AESGCMEncryptor
 * @see AESPBEEncryptor
 */
public interface IEncryptor {

    /**
     * 数据加密
     *
     * @param algorithm 加密算法
     * @param data      明文
     * @param key       密钥
     * @param password  口令
     * @return 密文
     */
    BytesToStr encrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password);

    /**
     * 数据解密
     *
     * @param algorithm 加密算法
     * @param data      密文
     * @param key       密钥
     * @param password  口令
     * @return 明文
     */
    BytesToStr decrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password);
}
