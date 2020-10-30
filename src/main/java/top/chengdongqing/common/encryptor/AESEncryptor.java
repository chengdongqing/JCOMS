package top.chengdongqing.common.encryptor;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES加解密工具
 *
 * @author Luyao
 */
public class AESEncryptor implements IEncryptor {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            // 生成16字节的随机数作为偏移向量，这样同样的明文每次生成的密文都是不一样的
            byte[] iv = SecureRandom.getInstanceStrong().generateSeed(16);
            AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            // 默认将iv放在密文前面一起当作密文返回，简化存储
            return mergeBytes(iv, cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 合并字节数组
     */
    public byte[] mergeBytes(byte[] bytes1, byte[] bytes2) {
        byte[] newBytes = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, newBytes, 0, bytes1.length);
        System.arraycopy(bytes2, 0, newBytes, bytes1.length, bytes2.length);
        return newBytes;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        try {
            // 分割iv和密文
            byte[] iv = new byte[16];
            byte[] cipherData = new byte[data.length - 16];
            System.arraycopy(data, 0, iv, 0, 16);
            System.arraycopy(data, 16, cipherData, 0, cipherData.length);
            // 解密数据
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(cipherData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key, String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEwithSHA256and128bitAES-CBC-BC");
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(key, 1000);
            Cipher cipher = Cipher.getInstance("PBEwithSHA256and128bitAES-CBC-BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key, String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEwithSHA256and128bitAES-CBC-BC");
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(key, 1000);
            Cipher cipher = Cipher.getInstance("PBEwithSHA256and128bitAES-CBC-BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}