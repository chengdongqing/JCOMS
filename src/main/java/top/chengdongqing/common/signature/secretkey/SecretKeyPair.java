package top.chengdongqing.common.signature.secretkey;

import top.chengdongqing.common.transformer.BytesToStr;

import java.util.Objects;

/**
 * Key pair model
 *
 * @author Luyao
 */
public record SecretKeyPair(BytesToStr privateKey, BytesToStr publicKey) {

    /**
     * Checks the key pair non null
     *
     * @param privateKey the private key
     * @param publicKey  the public key
     */
    public SecretKeyPair {
        Objects.requireNonNull(privateKey);
        Objects.requireNonNull(publicKey);
    }
}