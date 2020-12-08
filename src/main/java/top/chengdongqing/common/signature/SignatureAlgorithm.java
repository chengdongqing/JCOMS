package top.chengdongqing.common.signature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.chengdongqing.common.signature.signer.AsymmetricSigner;
import top.chengdongqing.common.signature.signer.MacSigner;
import top.chengdongqing.common.signature.signer.MessageDigestSigner;

/**
 * 签名算法枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum SignatureAlgorithm {

    MD5("MD", "MD5", MessageDigestSigner.class),
    SHA256("SHA", "SHA-256", MessageDigestSigner.class),
    HMAC_SHA1("HMAC", "HmacSHA1", MacSigner.class),
    HMAC_SHA256("HMAC", "HmacSHA256", MacSigner.class),
    RSA_SHA1("RSA", "sha1WithRSA", AsymmetricSigner.class),
    RSA_SHA256("RSA", "sha256WithRSA", AsymmetricSigner.class),
    DSA_SHA256("DSA", "sha256WithDSA", AsymmetricSigner.class),
    EcDSA_SHA256("EcDSA", "sha256WithECDSA", AsymmetricSigner.class),
    EdDSA_ED25519("EdDSA", "Ed25519", AsymmetricSigner.class);

    private final String family;
    private final String algorithm;
    private final Class<? extends DigitalSigner> signer;
}