package top.chengdongqing.common.signature;

import java.util.Objects;

/**
 * 密钥对
 *
 * @author Luyao
 */
public record SecretKeyPair(String privateKey, String publicKey) {

    /**
     * 校验参数是否为空
     *
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public SecretKeyPair {
        Objects.requireNonNull(privateKey);
        Objects.requireNonNull(publicKey);
    }
}