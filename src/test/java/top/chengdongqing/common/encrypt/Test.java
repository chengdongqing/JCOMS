package top.chengdongqing.common.encrypt;

import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;
import top.chengdongqing.common.transformer.BytesToStr;

import java.nio.charset.StandardCharsets;

/**
 * 加解密测试
 *
 * @author Luyao
 */
public class Test {

    public static void main(String[] args) {
        // 要加密的内容
        byte[] content = "3924893的回复就会加快jkdljflkdsf1.0-=".getBytes(StandardCharsets.UTF_8);

        test1(content);
        test2(content);
        test3(content);
        test4(content);
        test5(content);
    }

    private static void test1(byte[] content) {
        EncryptAlgorithm algorithm = EncryptAlgorithm.RSA_DEFAULT;
        System.out.println("基于%s的加密算法------------------".formatted(algorithm.getAlgorithm()));
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(SignatureAlgorithm.RSA_SHA256);
        System.out.println("公钥：" + keyPair.publicKey());
        System.out.println("私钥：" + keyPair.privateKey());
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, content, keyPair.publicKey(), null);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), keyPair.privateKey(), null);
        System.out.println("明文：" + plaintext.toText());
    }

    private static void test2(byte[] content) {
        EncryptAlgorithm algorithm = EncryptAlgorithm.RSA_ECB_OAEP;
        System.out.println("\n基于%s的加密算法------------------".formatted(algorithm.getAlgorithm()));
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(SignatureAlgorithm.RSA_SHA256);
        System.out.println("公钥：" + keyPair.publicKey());
        System.out.println("私钥：" + keyPair.privateKey());
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, content, keyPair.publicKey(), null);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), keyPair.privateKey(), null);
        System.out.println("明文：" + plaintext.toText());
    }

    private static void test3(byte[] content) {
        EncryptAlgorithm algorithm = EncryptAlgorithm.AES_CBC_PKCS7;
        System.out.println("\n基于%s的加密算法------------------".formatted(algorithm.getAlgorithm()));
        String key = StrKit.getRandomUUID();
        System.out.println("密钥：" + key);
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, content, key, null);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), key, null);
        System.out.println("明文：" + plaintext.toText());
    }

    private static void test4(byte[] content) {
        EncryptAlgorithm algorithm = EncryptAlgorithm.AES_GCM_NoPadding;
        System.out.println("\n基于%s的加密算法------------------".formatted(algorithm.getAlgorithm()));
        String key = StrKit.getRandomUUID();
        String associatedData = StrKit.getRandomUUID();
        System.out.println("密钥：" + key);
        System.out.println("关联数据：" + associatedData);
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, content, key, associatedData);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), key, associatedData);
        System.out.println("明文：" + plaintext.toText());
    }

    private static void test5(byte[] content) {
        EncryptAlgorithm algorithm = EncryptAlgorithm.AES_PBE;
        System.out.println("\n基于%s的加密算法------------------".formatted(algorithm.getAlgorithm()));
        String key = StrKit.getRandomUUID();
        String password = StrKit.getRandomUUID();
        System.out.println("密钥：" + key);
        System.out.println("口令：" + password);
        BytesToStr ciphertext = Encryptor.encrypt(algorithm, content, key, password);
        System.out.println("密文：" + ciphertext.toBase64());
        BytesToStr plaintext = Encryptor.decrypt(algorithm, ciphertext.bytes(), key, password);
        System.out.println("明文：" + plaintext.toText());
    }
}
