package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.IDigitalSigner;
import top.chengdongqing.common.signature.transform.SignBytes;
import top.chengdongqing.common.signature.SignatureAlgorithm;

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

    @Override
    public SignBytes signature(String content, byte[] key, SignatureAlgorithm algorithm) {
        try {
            Mac mac = Mac.getInstance(algorithm.getAlgorithm());
            mac.init(new SecretKeySpec(key, algorithm.getAlgorithm()));
            return SignBytes.of(mac.doFinal(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String content, byte[] key, SignatureAlgorithm algorithm, byte[] sign) {
        return Arrays.equals(signature(content, key, algorithm).bytes(), sign);
    }
}
