package top.chengdongqing.common.encryptor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 加密算法枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum EncryptAlgorithm {

    AES_CBC_PKCS7("AES", "AES/CBC/PKCS7Padding"),
    AES_GCM_NoPadding("AES", "AES/GCM/NoPadding"),
    RSA("RSA", "RSA"),
    RSA_ECB_OAEP("RSA", "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
    AES_PBE("AES", "PBEwithSHA256and128bitAES-CBC-BC");


    private final String familyName;
    // 加密算法/工作模式/填充模式
    private final String details;
}
