package top.chengdongqing.common.signature;

import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * 签名测试类
 *
 * @author Luyao
 */
public class Test {

    public static void main(String[] args) {
        // 要签名的内容
        String content = "dsfdshf8397584jksdhfjksdlfbvklbld875843 -={}/)#$";

        test1(content);
        test2(content);
        test3(content);
    }

    private static void test1(String content) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.EdDSA_ED25519;
        System.out.println("基于%s的数字签名----------------".formatted(algorithm.getAlgorithm()));
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(algorithm);
        System.out.println("私钥：" + keyPair.privateKey());
        System.out.println("公钥：" + keyPair.publicKey());
        BytesToStr sign = DigitalSigner.signature(algorithm, content, StrToBytes.of(keyPair.privateKey()).fromBase64());
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = DigitalSigner.verify(algorithm, content, StrToBytes.of(keyPair.publicKey()).fromBase64(),
                StrToBytes.of(sign.toBase64()).fromBase64());
        System.out.println("签名有效：" + isOk);
    }

    private static void test2(String content) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HMAC_SHA256;
        System.out.println("\n基于%s的数字签名----------------".formatted(algorithm.getAlgorithm()));
        BytesToStr key = SecretKeyGenerator.generateKey(algorithm);
        System.out.println("密钥（16进制字符串）：" + key.toHex());
        System.out.println("密钥（base64字符串）：" + key.toBase64());
        BytesToStr sign = DigitalSigner.signature(algorithm, content, StrToBytes.of(key.toHex()).fromHex());
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = DigitalSigner.verify(algorithm, content, StrToBytes.of(key.toHex()).fromHex(),
                StrToBytes.of(sign.toHex()).fromHex());
        System.out.println("签名有效：" + isOk);
    }

    private static void test3(String content) {
        SignatureAlgorithm algorithm = SignatureAlgorithm.SHA256;
        System.out.println("\n基于%s的数字签名----------------".formatted(algorithm.getAlgorithm()));
        BytesToStr sign = DigitalSigner.signature(algorithm, content, null);
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = DigitalSigner.verify(algorithm, content, null, StrToBytes.of(sign.toHex()).fromHex());
        System.out.println("签名有效：" + isOk);
    }
}
