package top.chengdongqing.common.signature;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 签名算法枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum SignatureAlgorithm {

    MD5("MD", "MD5"),
    SHA1("SHA", "SHA-1"),
    SHA256("SHA", "SHA-256"),
    HMAC_SHA1("HMAC", "HmacSHA1"),
    HMAC_SHA256("HMAC", "HmacSHA256"),
    SHA1_RSA("RSA", "sha1WithRSA"),
    SHA256_RSA("RSA", "sha256WithRSA"),
    SHA256_DSA("DSA", "sha256WithDSA"),
    SHA256_EcDSA("EC", "sha256WithECDSA"),
    EdDSA_ED25519("EdDSA", "Ed25519");

    private final String familyName;
    private final String algorithm;
}