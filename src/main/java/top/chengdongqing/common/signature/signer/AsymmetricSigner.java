package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.IDigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.transform.SignBytes;

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

    @Override
    public SignBytes signature(SignatureAlgorithm algorithm, String content, byte[] key) {
        try {
            Signature signature = Signature.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamily());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            signature.initSign(privateKey);
            signature.update(content.getBytes());
            return SignBytes.of(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(SignatureAlgorithm algorithm, String content, byte[] key, byte[] sign) {
        try {
            Signature signature = Signature.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamily());
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