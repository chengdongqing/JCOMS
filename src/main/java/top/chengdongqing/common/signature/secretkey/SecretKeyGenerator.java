package top.chengdongqing.common.signature.secretkey;

import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.crypto.KeyGenerator;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * The secret key generator
 *
 * @author Luyao
 */
public class SecretKeyGenerator {

    /**
     * Generates key pair by the signature algorithm
     *
     * @param algorithm the signature algorithm
     * @return the generated key pair
     */
    public static SecretKeyPair generateKeyPair(SignatureAlgorithm algorithm) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm.getFamily());
            if (algorithm == SignatureAlgorithm.RSA_SHA1) generator.initialize(1024);
            KeyPair keyPair = generator.generateKeyPair();
            BytesToStr privateKey = BytesToStr.of(keyPair.getPrivate().getEncoded());
            BytesToStr publicKey = BytesToStr.of(keyPair.getPublic().getEncoded());
            return new SecretKeyPair(privateKey, publicKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates key by the signature algorithm
     *
     * @param algorithm the signature algorithm
     * @return the generated key
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
