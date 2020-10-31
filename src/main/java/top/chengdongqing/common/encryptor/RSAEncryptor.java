package top.chengdongqing.common.encryptor;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA非对称加解密器
 * 适用小数据量加解密，如aes secret key
 *
 * @author Luyao
 */
public class RSAEncryptor implements IEncryptor {

    private static final String ALGORITHM = "RSA";

    private static class EncryptorHolder {
        private static final RSAEncryptor ME = new RSAEncryptor();
    }

    public static RSAEncryptor me() {
        return EncryptorHolder.ME;
    }

    @Override
    public byte[] encrypt(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
