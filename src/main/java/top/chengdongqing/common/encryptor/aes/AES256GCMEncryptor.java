package top.chengdongqing.common.encryptor.aes;

import lombok.Data;
import top.chengdongqing.common.encryptor.IEncryptor;
import top.chengdongqing.common.encryptor.entity.DecryptEntity;
import top.chengdongqing.common.encryptor.entity.EncryptEntity;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @author Luyao
 */
public class AES256GCMEncryptor implements IEncryptor {

    private static final int IV_LENGTH_BIT = 16;
    private static final int TAG_LENGTH_BIT = 128;

    @Override
    public DecryptEntity encrypt(EncryptEntity entity1) {
        if (entity1 instanceof AES256GCMEncryptEntity entity) {
            try {
                Cipher cipher = Cipher.getInstance(entity.getAlgorithm().getDetails());
                SecretKeySpec keySpec = new SecretKeySpec(entity.getKey().getBytes(), entity.getAlgorithm().getFamilyName());
                // 生成16字节的随机数作为初始向量，这样同样的明文每次生成的密文都是不一样的
                byte[] iv = SecureRandom.getInstanceStrong().generateSeed(IV_LENGTH_BIT);
                GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
                cipher.updateAAD(entity.getAssociatedData());
                return new AES256GCMDecryptEntity(cipher.doFinal(entity.getData()), iv, entity.getAssociatedData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("The EncryptionEntity is error.");
        }
    }

    @Override
    public byte[] decrypt(EncryptEntity entity1) {
        if (entity1 instanceof AES256GCMEncryptEntity entity) {
            try {
                Cipher cipher = Cipher.getInstance(entity.getAlgorithm().getDetails());
                SecretKeySpec keySpec = new SecretKeySpec(entity.getKey().getBytes(), entity.getAlgorithm().getFamilyName());
                GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, entity.getNonceStr());
                cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
                cipher.updateAAD(entity.getAssociatedData());
                return cipher.doFinal(entity.getData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("The EncryptionEntity is error.");
        }
    }

    @Data
    public static class AES256GCMEncryptEntity extends EncryptEntity {
        private byte[] nonceStr, associatedData;
    }

    @Data
    public static class AES256GCMDecryptEntity extends DecryptEntity {
        private byte[] nonceStr, associatedData;

        public AES256GCMDecryptEntity(byte[] ciphertext, byte[] nonceStr, byte[] associatedData) {
            super.ciphertext = ciphertext;
            this.nonceStr = nonceStr;
            this.associatedData = associatedData;
        }
    }
}
