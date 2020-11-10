package top.chengdongqing.common.encrypt.encryptor;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import top.chengdongqing.common.encrypt.EncryptAlgorithm;
import top.chengdongqing.common.encrypt.IEncryptor;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;

/**
 * CBC (Cipher Block Chaining) 密码分组链接模式
 * 在每次加密之前或者解密之后，使用IV与明文或密文异或。
 * IV (Initialization Vector) 初始向量
 * 异或：不进位的加法运算
 *
 * @author Luyao
 */
public class AESCBCEncryptor implements IEncryptor {

    private static final int IV_LENGTH_BIT = 16;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public BytesToStr encrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password) {
        try {
            // 加密
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm.getFamily());
            byte[] iv = SecureRandom.getInstanceStrong().generateSeed(IV_LENGTH_BIT);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            // 合并初始向量和密文
            return BytesToStr.of(ByteUtils.concatenate(iv, cipher.doFinal(data)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BytesToStr decrypt(EncryptAlgorithm algorithm, byte[] data, String key, String password) {
        try {
            // 分割初始向量和密文
            byte[][] bytes = ByteUtils.split(data, IV_LENGTH_BIT);
            byte[] iv = bytes[0];
            byte[] ciphertext = bytes[1];
            // 解密
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm.getFamily());
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            return BytesToStr.of(cipher.doFinal(ciphertext));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
