package top.chengdongqing.common.encryptor;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA非对称加解密器
 * 适用小数据量加解密，如aes secret key
 *
 * @author Luyao
 */
public class RSAEncryptor implements IEncryptor {

    private static final String ALGORITHM = "RSA";

    @Override
    public byte[] encrypt(byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key.getBytes());
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
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key.getBytes());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
