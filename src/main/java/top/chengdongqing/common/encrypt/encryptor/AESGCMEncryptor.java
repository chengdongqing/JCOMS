package top.chengdongqing.common.encrypt.encryptor;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import top.chengdongqing.common.encrypt.EncryptAlgorithm;
import top.chengdongqing.common.encrypt.IEncryptor;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * GCM (Galois/Counter Mode) 指的是该对称加密采用Counter模式，并带有GMAC消息认证码。
 * GCM可以提供对消息的加密和完整性校验，另外，它还可以提供附加消息的完整性校验。
 *
 * @author Luyao
 */
public class AESGCMEncryptor implements IEncryptor {

    private static final int NONCE_LENGTH_BIT = 12;
    private static final int TAG_LENGTH_BIT = 128;

    @Override
    public BytesToStr encrypt(EncryptAlgorithm algorithm, byte[] data, String key, String associatedData) {
        try {
            // 加密
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm.getFamily());
            byte[] iv = SecureRandom.getInstanceStrong().generateSeed(NONCE_LENGTH_BIT);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
            cipher.updateAAD(associatedData.getBytes());
            // 合并初始向量和密文
            return BytesToStr.of(ByteUtils.concatenate(iv, cipher.doFinal(data)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BytesToStr decrypt(EncryptAlgorithm algorithm, byte[] data, String key, String associatedData) {
        try {
            // 分割初始向量和密文
            byte[][] bytes = ByteUtils.split(data, NONCE_LENGTH_BIT);
            byte[] iv = bytes[0];
            byte[] ciphertext = bytes[1];
            // 解密
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm.name());
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
            cipher.updateAAD(associatedData.getBytes());
            return BytesToStr.of(cipher.doFinal(ciphertext));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
