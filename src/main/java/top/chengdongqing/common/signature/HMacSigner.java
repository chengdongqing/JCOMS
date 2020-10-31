package top.chengdongqing.common.signature;

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
    public static Bytes signatureForBase64(String content, String key, SignatureAlgorithm algorithm) {
        return SignerHolder.SIGNER.signature(content, ToBytes.of(key).fromBase64(), algorithm);
    }

    /**
     * 执行签名
     * 这里的key仅接受16进制数据
     */
    public static Bytes signatureForHex(String content, String key, SignatureAlgorithm algorithm) {
        return SignerHolder.SIGNER.signature(content, ToBytes.of(key).fromHex(), algorithm);
    }

    /**
     * 执行验签
     * 这里的key和sign仅接受base64形式
     */
    public static boolean verifyForBase64(String content, String key, SignatureAlgorithm algorithm, String sign) {
        return SignerHolder.SIGNER.verify(content, ToBytes.of(key).fromBase64(), algorithm, ToBytes.of(sign).fromBase64());
    }

    /**
     * 执行验签
     * 这里的key和sign仅接受16进制数据
     */
    public static boolean verifyForHex(String content, String key, SignatureAlgorithm algorithm, String sign) {
        return SignerHolder.SIGNER.verify(content, ToBytes.of(key).fromHex(), algorithm, ToBytes.of(sign).fromHex());
    }

    @Override
    public Bytes signature(String content, byte[] key, SignatureAlgorithm algorithm) {
        try {
            Mac mac = Mac.getInstance(algorithm.getAlgorithm());
            mac.init(new SecretKeySpec(key, algorithm.getAlgorithm()));
            return Bytes.of(mac.doFinal(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String content, byte[] key, SignatureAlgorithm algorithm, byte[] sign) {
        return Arrays.equals(signature(content, key, algorithm).bytes(), sign);
    }
}
