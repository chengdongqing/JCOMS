package top.chengdongqing.common.encryptor;

import top.chengdongqing.common.encryptor.entity.DecryptEntity;
import top.chengdongqing.common.encryptor.entity.EncryptEntity;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
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

    @Override
    public DecryptEntity encrypt(EncryptEntity entity) {
        try {
            Cipher cipher = Cipher.getInstance(entity.getAlgorithm().getDetails());
            KeyFactory keyFactory = KeyFactory.getInstance(entity.getAlgorithm().getFamilyName());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(entity.getKey()));
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return new DecryptEntity(cipher.doFinal(entity.getData()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(EncryptEntity entity) {
        try {
            Cipher cipher = Cipher.getInstance(entity.getAlgorithm().getDetails());
            KeyFactory keyFactory = KeyFactory.getInstance(entity.getAlgorithm().getFamilyName());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(entity.getKey()));
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(entity.getData());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
