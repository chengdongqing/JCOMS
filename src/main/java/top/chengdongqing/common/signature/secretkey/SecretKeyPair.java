package top.chengdongqing.common.signature.secretkey;

import top.chengdongqing.common.transformer.BytesToStr;

import java.util.Objects;

/**
 * 密钥对
 *
 * @author Luyao
 */
public record SecretKeyPair(BytesToStr privateKey, BytesToStr publicKey) {

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