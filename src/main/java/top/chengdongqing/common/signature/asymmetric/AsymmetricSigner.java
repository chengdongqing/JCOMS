package top.chengdongqing.common.signature.asymmetric;

import top.chengdongqing.common.signature.ByteArray;
import top.chengdongqing.common.signature.IDigitalSigner;
import top.chengdongqing.common.signature.SignAlgorithm;
import top.chengdongqing.common.signature.ToByteArray;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 数字签名
 * 基于非对称加密算法
 *
 * @author Luyao
 */
public class AsymmetricSigner implements IDigitalSigner {

    private static class SignerHolder {
        private static final AsymmetricSigner SIGNER = new AsymmetricSigner();
    }

    /**
     * 执行签名
     * 这里的key仅接受base64形式
     */
    public static ByteArray signature(String content, String key, SignAlgorithm algorithm) {
        return SignerHolder.SIGNER.signature(content, ToByteArray.of(key).fromBase64(), algorithm);
    }

    /**
     * 执行验签
     * 这里的key和sign仅接受base64形式
     */
    public static boolean validate(String content, String key, SignAlgorithm algorithm, String sign) {
        return SignerHolder.SIGNER.validate(content, ToByteArray.of(key).fromBase64(), algorithm, ToByteArray.of(sign).fromBase64());
    }

    /**
     * 执行签名
     *
     * @param content   签名的内容
     * @param key       私钥
     * @param algorithm 签名算法
     * @return 数字签名
     */
    @Override
    public ByteArray signature(String content, byte[] key, SignAlgorithm algorithm) {
        try {
            Signature signature = Signature.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamilyName());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            signature.initSign(privateKey);
            signature.update(content.getBytes());
            return ByteArray.of(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证签名
     *
     * @param content   签名的内容
     * @param key       公钥
     * @param algorithm 签名算法
     * @param sign      要校验的签名
     * @return 签名是否正确
     */
    @Override
    public boolean validate(String content, byte[] key, SignAlgorithm algorithm, byte[] sign) {
        try {
            Signature signature = Signature.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamilyName());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            signature.initVerify(publicKey);
            signature.update(content.getBytes());
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}