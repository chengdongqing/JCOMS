package top.chengdongqing.common.encrypt.encryptor;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import top.chengdongqing.common.encrypt.EncryptAlgorithm;
import top.chengdongqing.common.encrypt.IEncryptor;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Security;
import java.util.Base64;

/**
 * PBE加解密
 * 支持自定义口令，类似锁屏密码，可以任意长度
 * 口令将和固定长度的密钥混合
 *
 * @author Luyao
 */
public class AESPBEEncryptor implements IEncryptor {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public BytesToStr encrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm.getAlgorithm());
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(key.getBytes(), 1000);
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            return BytesToStr.of(cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BytesToStr decrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm.getAlgorithm());
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(key.getBytes(), 1000);
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            return BytesToStr.of(cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
