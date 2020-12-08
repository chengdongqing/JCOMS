package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 数字签名
 * 基于非对称加密算法
 *
 * @author Luyao
 */
public record AsymmetricSigner(SignatureAlgorithm algorithm) implements DigitalSigner {

    @Override
    public BytesToStr signature(String content, byte[] privateKey) {
        try {
            Signature signature = Signature.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamily());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
            signature.initSign(keyFactory.generatePrivate(keySpec));
            signature.update(content.getBytes());
            return BytesToStr.of(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String content, byte[] sign, byte[] publicKey) {
        try {
            Signature signature = Signature.getInstance(algorithm.getAlgorithm());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getFamily());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            signature.initVerify(keyFactory.generatePublic(keySpec));
            signature.update(content.getBytes());
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}