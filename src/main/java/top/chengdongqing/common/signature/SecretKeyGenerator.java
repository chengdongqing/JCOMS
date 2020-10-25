package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.asymmetric.SecretKeyPair;

import javax.crypto.KeyGenerator;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 密钥生成器
 *
 * @author Luyao
 */
public class SecretKeyGenerator {

    /**
     * 生成密钥对
     *
     * @param algorithm 签名算法
     * @return 密钥对
     */
    public static SecretKeyPair generateKeyPair(SignAlgorithm algorithm) {
        try {
            // 获取指定算法的密钥对生成器
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm.getFamilyName());
            // 如果是sha1WithRSA指定生成的密钥长度，否则用默认的长度
            if (algorithm == SignAlgorithm.SHA1_RSA) generator.initialize(1024);
            // 生成密钥对
            KeyPair keyPair = generator.generateKeyPair();
            // 将私钥和公钥通过Base64编码
            Base64.Encoder encoder = Base64.getEncoder();
            String privateKey = encoder.encodeToString(keyPair.getPrivate().getEncoded());
            String publicKey = encoder.encodeToString(keyPair.getPublic().getEncoded());
            return new SecretKeyPair(privateKey, publicKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成密钥
     *
     * @param algorithm 签名算法
     * @return 密钥
     */
    public static ByteArray generateKey(SignAlgorithm algorithm) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.getAlgorithm());
            return ByteArray.of(keyGenerator.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}