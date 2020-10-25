package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.asymmetric.AsymmetricSigner;
import top.chengdongqing.common.signature.asymmetric.SecretKeyPair;
import top.chengdongqing.common.signature.digest.MessageDigestSigner;
import top.chengdongqing.common.signature.hmac.HMacSigner;

/**
 * 签名测试类
 *
 * @author Luyao
 */
public class Test {

    public static void main(String[] args) {
        // 要签名的内容
        String content = "dsfdshf8397584jksdhfjksdlfbvklbld";

        test1(content, SignAlgorithm.EdDSA_ED25519);
        test2(content, SignAlgorithm.HMAC_SHA256);
        test3(content, SignAlgorithm.SHA256);
    }

    private static void test1(String content, SignAlgorithm algorithm) {
        System.out.println("基于非对称加密算法的数字签名----------------");
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(algorithm);
        System.out.println("私钥：" + keyPair.privateKey());
        System.out.println("公钥：" + keyPair.publicKey());
        ByteArray sign = AsymmetricSigner.signature(content, keyPair.privateKey(), algorithm);
        System.out.println("签名（16进制）：" + sign.toHex());
        System.out.println("签名（base64形式）：" + sign.toBase64());
        boolean isOk = AsymmetricSigner.validate(content, keyPair.publicKey(), algorithm, sign.toBase64());
        System.out.println("签名有效：" + isOk);
    }

    private static void test2(String content, SignAlgorithm algorithm) {
        System.out.println("基于带消息认证码的消息摘要算法的数字签名----------------");
        ByteArray key = SecretKeyGenerator.generateKey(algorithm);
        System.out.println("密钥（16进制）：" + key.toHex());
        System.out.println("密钥（base64形式）：" + key.toBase64());
        ByteArray sign = HMacSigner.signatureForHex(content, key.toHex(), algorithm);
        System.out.println("签名（16进制）：" + sign.toHex());
        System.out.println("签名（base64形式）：" + sign.toBase64());
        boolean isOk = HMacSigner.validateForHex(content, key.toHex(), algorithm, sign.toHex());
        System.out.println("签名有效：" + isOk);
    }

    private static void test3(String content, SignAlgorithm algorithm) {
        System.out.println("基于消息摘要算法的数字签名----------------");
        ByteArray sign = MessageDigestSigner.signature(content, algorithm);
        System.out.println("签名（16进制）：" + sign.toHex());
        System.out.println("签名（base64形式）：" + sign.toBase64());
        boolean isOk = MessageDigestSigner.validate(content, algorithm, sign.toHex());
        System.out.println("签名有效：" + isOk);
    }
}