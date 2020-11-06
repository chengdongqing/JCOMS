package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;
import top.chengdongqing.common.signature.transform.SignBytes;
import top.chengdongqing.common.signature.transform.StrToBytes;

/**
 * 签名测试类
 *
 * @author Luyao
 */
public class Test {

    public static void main(String[] args) {
        // 要签名的内容
        String content = "dsfdshf8397584jksdhfjksdlfbvklbld";

        test1(content, SignatureAlgorithm.EdDSA_ED25519);
        test2(content, SignatureAlgorithm.HMAC_SHA256);
        test3(content, SignatureAlgorithm.SHA256);
    }

    private static void test1(String content, SignatureAlgorithm algorithm) {
        System.out.println("基于非对称加密算法的数字签名----------------");
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(algorithm);
        System.out.println("私钥：" + keyPair.privateKey());
        System.out.println("公钥：" + keyPair.publicKey());
        SignBytes sign = DigitalSigner.signature(content, StrToBytes.of(keyPair.privateKey()).toBytesFromBase64(), algorithm);
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = DigitalSigner.verify(content, StrToBytes.of(keyPair.publicKey()).toBytesFromBase64(),
                algorithm, StrToBytes.of(sign.toBase64()).toBytesFromBase64());
        System.out.println("签名有效：" + isOk);
    }

    private static void test2(String content, SignatureAlgorithm algorithm) {
        System.out.println("基于带消息认证码的消息摘要算法的数字签名----------------");
        SignBytes key = SecretKeyGenerator.generateKey(algorithm);
        System.out.println("密钥（16进制字符串）：" + key.toHex());
        System.out.println("密钥（base64字符串）：" + key.toBase64());
        SignBytes sign = DigitalSigner.signature(content, StrToBytes.of(key.toHex()).toBytesFromHex(), algorithm);
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = DigitalSigner.verify(content, StrToBytes.of(key.toHex()).toBytesFromHex(),
                algorithm, StrToBytes.of(sign.toHex()).toBytesFromHex());
        System.out.println("签名有效：" + isOk);
    }

    private static void test3(String content, SignatureAlgorithm algorithm) {
        System.out.println("基于消息摘要算法的数字签名----------------");
        SignBytes sign = DigitalSigner.signature(content, null, algorithm);
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = DigitalSigner.verify(content, null, algorithm, StrToBytes.of(sign.toHex()).toBytesFromHex());
        System.out.println("签名有效：" + isOk);
    }
}
