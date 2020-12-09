package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.signer.AsymmetricSigner;
import top.chengdongqing.common.signature.signer.MacSigner;
import top.chengdongqing.common.signature.signer.MessageDigestSigner;
import top.chengdongqing.common.transformer.BytesToStr;

/**
 * The Digital signer
 *
 * @author Luyao
 * @see AsymmetricSigner
 * @see MacSigner
 * @see MessageDigestSigner
 */
public interface DigitalSigner {

    /**
     * Creates a new signer instance by the signature algorithm
     *
     * @param algorithm the signature algorithm
     * @return the signer instance
     */
    static DigitalSigner newInstance(SignatureAlgorithm algorithm) {
        try {
            return algorithm.getSigner().getDeclaredConstructor(SignatureAlgorithm.class).newInstance(algorithm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes signature
     *
     * @param content the content to signature
     * @param key     the key for signature
     * @return the digital signature of the content
     */
    BytesToStr signature(String content, byte[] key);

    /**
     * Verifies signature
     *
     * @param content the signed content of the {@code sign}
     * @param sign    the signature to verify
     * @param key     the key for verify
     * @return {@code true} if the {@code sign} is true
     */
    boolean verify(String content, byte[] sign, byte[] key);
}
