package top.chengdongqing.common.encryptor;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import top.chengdongqing.common.encryptor.aes.AESEncryptorContext;
import top.chengdongqing.common.encryptor.entity.DecryptEntity;
import top.chengdongqing.common.encryptor.entity.EncryptEntity;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * AES对称加解密器
 * 适用大数据量加解密
 *
 * @author Luyao
 */
public class AESEncryptor implements IEncryptor {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static class EncryptorHolder {
        private static final AESEncryptor ME = new AESEncryptor();
    }

    public static AESEncryptor me() {
        return EncryptorHolder.ME;
    }

    @Override
    public DecryptEntity encrypt(EncryptEntity entity) {
        return new AESEncryptorContext(entity.getAlgorithm()).encrypt(entity);
    }

    @Override
    public byte[] decrypt(EncryptEntity entity) {
        return new AESEncryptorContext(entity.getAlgorithm()).decrypt(entity);
    }

    /**
     * 带口令的数据加密
     *
     * @param data     明文
     * @param key      密钥
     * @param password 口令
     * @return 密文
     */
    public byte[] encrypt(byte[] data, String key, String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(EncryptAlgorithm.AES_PBE.getDetails());
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(Base64.getDecoder().decode(key), 1000);
            Cipher cipher = Cipher.getInstance(EncryptAlgorithm.AES_PBE.getDetails());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 带口令的数据解密
     *
     * @param data     待解密数据
     * @param key      密钥
     * @param password 口令
     * @return 明文
     */
    public byte[] decrypt(byte[] data, String key, String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(EncryptAlgorithm.AES_PBE.getDetails());
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(Base64.getDecoder().decode(key), 1000);
            Cipher cipher = Cipher.getInstance(EncryptAlgorithm.AES_PBE.getDetails());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}