package top.chengdongqing.common.encrypt;

import org.junit.Test;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * 加解密测试
 *
 * @author Luyao
 */
public class EncryptTest {

    private static final byte[] CONTENT = StrToBytes.of("3924893的回复就会加快jkdljflkdsf1.0-=").fromText();

    @Test
    public void test1() {
        EncryptAlgorithm algorithm = EncryptAlgorithm.RSA_DEFAULT;
        System.out.printf("基于%s的加密算法------------------%n", algorithm.getAlgorithm());
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(SignatureAlgorithm.RSA_SHA256);
        System.out.println("公钥：" + keyPair.publicKey().toBase64());
        System.out.println("私钥：" + keyPair.publicKey().toBase64());
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, CONTENT, keyPair.publicKey().toBase64(), null);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), keyPair.privateKey().toBase64(), null);
        System.out.println("明文：" + plaintext.toText());
    }

    @Test
    public void test2() {
        EncryptAlgorithm algorithm = EncryptAlgorithm.RSA_ECB_OAEP;
        System.out.printf("%n基于%s的加密算法------------------%n", algorithm.getAlgorithm());
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(SignatureAlgorithm.RSA_SHA256);
        System.out.println("公钥：" + keyPair.publicKey().toBase64());
        System.out.println("私钥：" + keyPair.publicKey().toBase64());
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, CONTENT, keyPair.publicKey().toBase64(), null);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), keyPair.privateKey().toBase64(), null);
        System.out.println("明文：" + plaintext.toText());
    }

    @Test
    public void test3() {
        EncryptAlgorithm algorithm = EncryptAlgorithm.AES_CBC_PKCS7;
        System.out.printf("%n基于%s的加密算法------------------%n", algorithm.getAlgorithm());
        String key = StrKit.getRandomUUID();
        System.out.println("密钥：" + key);
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, CONTENT, key, null);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), key, null);
        System.out.println("明文：" + plaintext.toText());
    }

    @Test
    public void test4() {
        EncryptAlgorithm algorithm = EncryptAlgorithm.AES_GCM_NoPadding;
        System.out.printf("%n基于%s的加密算法------------------%n", algorithm.getAlgorithm());
        String key = StrKit.getRandomUUID();
        String associatedData = StrKit.getRandomUUID();
        System.out.println("密钥：" + key);
        System.out.println("关联数据：" + associatedData);
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, CONTENT, key, associatedData);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), key, associatedData);
        System.out.println("明文：" + plaintext.toText());
    }

    @Test
    public void test5() {
        EncryptAlgorithm algorithm = EncryptAlgorithm.AES_PBE;
        System.out.printf("%n基于%s的加密算法------------------%n", algorithm.getAlgorithm());
        String key = StrKit.getRandomUUID();
        String password = StrKit.getRandomUUID();
        System.out.println("密钥：" + key);
        System.out.println("口令：" + password);
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, CONTENT, key, password);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), key, password);
        System.out.println("明文：" + plaintext.toText());
    }
}
