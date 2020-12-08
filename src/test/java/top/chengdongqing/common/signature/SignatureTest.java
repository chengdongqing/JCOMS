package top.chengdongqing.common.signature;

import org.junit.Test;
import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * 签名测试
 *
 * @author Luyao
 */
public class SignatureTest {

    private static final String CONTENT = "dsfdshf8397584jksdhfjksdlfbvklbld875843 -={}/)#$";

    @Test
    public void test1() {
        SignatureAlgorithm algorithm = SignatureAlgorithm.EdDSA_ED25519;
        DigitalSigner signer = DigitalSigner.newInstance(algorithm);
        System.out.printf("基于%s的数字签名----------------%n", algorithm.getAlgorithm());
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(algorithm);
        System.out.println("私钥：" + keyPair.privateKey().toBase64());
        System.out.println("公钥：" + keyPair.publicKey().toBase64());
        BytesToStr sign = signer.signature(CONTENT, StrToBytes.of(keyPair.privateKey().toBase64()).fromBase64());
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = signer.verify(CONTENT, sign.bytes(), StrToBytes.of(keyPair.publicKey().toBase64()).fromBase64());
        System.out.println("签名有效：" + isOk);
    }

    @Test
    public void test2() {
        SignatureAlgorithm algorithm = SignatureAlgorithm.HMAC_SHA256;
        DigitalSigner signer = DigitalSigner.newInstance(algorithm);
        System.out.printf("%n基于%s的数字签名----------------%n", algorithm.getAlgorithm());
        BytesToStr key = SecretKeyGenerator.generateKey(algorithm);
        System.out.println("密钥（16进制字符串）：" + key.toHex());
        System.out.println("密钥（base64字符串）：" + key.toBase64());
        BytesToStr sign = signer.signature(CONTENT, StrToBytes.of(key.toHex()).fromHex());
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = signer.verify(CONTENT, sign.bytes(), key.bytes());
        System.out.println("签名有效：" + isOk);
    }

    @Test
    public void test3() {
        SignatureAlgorithm algorithm = SignatureAlgorithm.MD5;
        DigitalSigner signer = DigitalSigner.newInstance(algorithm);
        System.out.printf("%n基于%s的数字签名----------------%n", algorithm.getAlgorithm());
        BytesToStr sign = signer.signature(CONTENT, null);
        System.out.println("签名（16进制字符串）：" + sign.toHex());
        System.out.println("签名（base64字符串）：" + sign.toBase64());
        boolean isOk = signer.verify(CONTENT, sign.bytes(), null);
        System.out.println("签名有效：" + isOk);
    }
}
