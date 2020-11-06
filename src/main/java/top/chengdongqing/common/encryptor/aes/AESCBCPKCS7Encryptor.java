package top.chengdongqing.common.encryptor.aes;

import lombok.Data;
import top.chengdongqing.common.encryptor.IEncryptor;
import top.chengdongqing.common.encryptor.entity.DecryptEntity;
import top.chengdongqing.common.encryptor.entity.EncryptEntity;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @author Luyao
 */
public class AESCBCPKCS7Encryptor implements IEncryptor {

    /**
     * IV长度，单位：位/比特
     */
    private static final int IV_LENGTH_BIT = 16;

    @Override
    public DecryptEntity encrypt(EncryptEntity entity) {
        try {
            Cipher cipher = Cipher.getInstance(entity.getAlgorithm().getDetails());
            SecretKeySpec keySpec = new SecretKeySpec(entity.getKey().getBytes(), entity.getAlgorithm().getFamilyName());
            // 生成16字节的随机数作为初始向量，这样同样的明文每次生成的密文都是不一样的
            byte[] iv = SecureRandom.getInstanceStrong().generateSeed(IV_LENGTH_BIT);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return new AESCBCPKCS7DecryptEntity(cipher.doFinal(entity.getData()), iv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(EncryptEntity entity1) {
        if (entity1 instanceof AESCBCPKCS7EncryptEntity entity) {
            try {
                // 解密数据
                Cipher cipher = Cipher.getInstance(entity.getAlgorithm().getDetails());
                SecretKeySpec keySpec = new SecretKeySpec(entity.getKey().getBytes(), entity.getAlgorithm().getFamilyName());
                IvParameterSpec ivParameterSpec = new IvParameterSpec(entity.getIv());
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
                return cipher.doFinal(entity.getData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("The EncryptionEntity is error.");
        }
    }

    @Data
    public static class AESCBCPKCS7DecryptEntity extends DecryptEntity {
        private byte[] iv;

        public AESCBCPKCS7DecryptEntity(byte[] data, byte[] iv) {
            super.ciphertext = data;
            this.iv = iv;
        }
    }

    @Data
    public static class AESCBCPKCS7EncryptEntity extends EncryptEntity {
        private byte[] iv;
    }
}
