package top.chengdongqing.common.signature.hmac;

import top.chengdongqing.common.signature.ByteArray;
import top.chengdongqing.common.signature.IDigitalSigner;
import top.chengdongqing.common.signature.SignAlgorithm;
import top.chengdongqing.common.signature.ToByteArray;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * 数字签名
 * 基于带消息认证码的消息摘要算法
 *
 * @author Luyao
 */
public class HMacSigner implements IDigitalSigner {

    private static class SignerHolder {
        private static final HMacSigner SIGNER = new HMacSigner();
    }

    /**
     * 执行签名
     * 这里的key仅接受base64形式
     */
    public static ByteArray signatureForBase64(String content, String key, SignAlgorithm algorithm) {
        return SignerHolder.SIGNER.signature(content, ToByteArray.of(key).fromBase64(), algorithm);
    }

    /**
     * 执行签名
     * 这里的key仅接受16进制数据
     */
    public static ByteArray signatureForHex(String content, String key, SignAlgorithm algorithm) {
        return SignerHolder.SIGNER.signature(content, ToByteArray.of(key).fromHex(), algorithm);
    }

    /**
     * 执行验签
     * 这里的key和sign仅接受base64形式
     */
    public static boolean validateForBase64(String content, String key, SignAlgorithm algorithm, String sign) {
        return SignerHolder.SIGNER.validate(content, ToByteArray.of(key).fromBase64(), algorithm, ToByteArray.of(sign).fromBase64());
    }

    /**
     * 执行验签
     * 这里的key和sign仅接受16进制数据
     */
    public static boolean validateForHex(String content, String key, SignAlgorithm algorithm, String sign) {
        return SignerHolder.SIGNER.validate(content, ToByteArray.of(key).fromHex(), algorithm, ToByteArray.of(sign).fromHex());
    }

    @Override
    public ByteArray signature(String content, byte[] key, SignAlgorithm algorithm) {
        try {
            Mac mac = Mac.getInstance(algorithm.getAlgorithm());
            mac.init(new SecretKeySpec(key, algorithm.getAlgorithm()));
            return ByteArray.of(mac.doFinal(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validate(String content, byte[] key, SignAlgorithm algorithm, byte[] sign) {
        return Arrays.equals(signature(content, key, algorithm).bytes(), sign);
    }
}
