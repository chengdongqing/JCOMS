package top.chengdongqing.common.encrypt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.chengdongqing.common.encrypt.encryptor.AESCBCEncryptor;
import top.chengdongqing.common.encrypt.encryptor.AESGCMEncrypt;
import top.chengdongqing.common.encrypt.encryptor.AESPBEEncryptor;
import top.chengdongqing.common.encrypt.encryptor.RSAEncryptor;

/**
 * 加密算法枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum EncryptAlgorithm {

    RSA_DEFAULT("RSA", "RSA", RSAEncryptor.class),
    RSA_ECB_OAEP("RSA", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding", RSAEncryptor.class),
    AES_CBC_PKCS7("AES", "AES/CBC/PKCS7Padding", AESCBCEncryptor.class),
    AES_GCM_NoPadding("AES", "AES/GCM/NoPadding", AESGCMEncrypt.class),
    AES_PBE("AES", "PBEwithSHA256and128bitAES-CBC-BC", AESPBEEncryptor.class);

    private final String family;
    private final String algorithm;
    private final Class<? extends IEncryptor> encryptor;
}
