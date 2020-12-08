package top.chengdongqing.common.encrypt.encryptor;

import top.chengdongqing.common.encrypt.EncryptAlgorithm;
import top.chengdongqing.common.encrypt.Encryptor;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA非对称加解密器
 * 公钥加密，私钥解密，更安全
 * 适用小数据量加解密，如密钥
 *
 * @author Luyao
 */
public record RSAEncryptor(EncryptAlgorithm algorithm) implements Encryptor {

    @Override
    public BytesToStr encrypt(byte[] data, String key, String password) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamily());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(StrToBytes.of(key).fromBase64());
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return BytesToStr.of(cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BytesToStr decrypt(byte[] data, String key, String password) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamily());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(StrToBytes.of(key).fromBase64());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return BytesToStr.of(cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
