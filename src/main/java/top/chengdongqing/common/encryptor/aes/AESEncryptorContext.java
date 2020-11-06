package top.chengdongqing.common.encryptor.aes;

import top.chengdongqing.common.encryptor.EncryptAlgorithm;
import top.chengdongqing.common.encryptor.IEncryptor;
import top.chengdongqing.common.encryptor.entity.DecryptEntity;
import top.chengdongqing.common.encryptor.entity.EncryptEntity;

/**
 * AES加密器上下文
 *
 * @author Luyao
 */
public class AESEncryptorContext {

    private IEncryptor encryptor;

    private AESEncryptorContext() {
    }

    public AESEncryptorContext(EncryptAlgorithm algorithm) {
        if (algorithm == EncryptAlgorithm.AES_CBC_PKCS7) {
            this.encryptor = new AESCBCPKCS7Encryptor();
        } else if (algorithm == EncryptAlgorithm.AES_GCM_NoPadding) {
            this.encryptor = new AES256GCMEncryptor();
        }
    }

    public DecryptEntity encrypt(EncryptEntity entity) {
        return encryptor.encrypt(entity);
    }

    public byte[] decrypt(EncryptEntity entity) {
        return encryptor.decrypt(entity);
    }
}
