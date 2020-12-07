package top.chengdongqing.common.signature.secretkey;

import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.crypto.KeyGenerator;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

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
    public static SecretKeyPair generateKeyPair(SignatureAlgorithm algorithm) {
        try {
            // 获取指定算法的密钥对生成器
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm.getFamily());
            // 如果是RSA1则指定生成的密钥长度，否则用默认的长度
            if (algorithm == SignatureAlgorithm.RSA_SHA1) generator.initialize(1024);
            // 生成密钥对
            KeyPair keyPair = generator.generateKeyPair();
            // 将私钥和公钥通过Base64编码
            BytesToStr privateKey = BytesToStr.of(keyPair.getPrivate().getEncoded());
            BytesToStr publicKey = BytesToStr.of(keyPair.getPublic().getEncoded());
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
    public static BytesToStr generateKey(SignatureAlgorithm algorithm) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.getAlgorithm());
            return BytesToStr.of(keyGenerator.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
