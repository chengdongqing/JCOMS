package top.chengdongqing.common.signature.signer;

import top.chengdongqing.common.signature.IDigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.transform.SignBytes;

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
    public SignBytes signature(SignatureAlgorithm algorithm, String content, byte[] key) {
        try {
            Mac mac = Mac.getInstance(algorithm.getAlgorithm());
            mac.init(new SecretKeySpec(key, algorithm.getAlgorithm()));
            return SignBytes.of(mac.doFinal(content.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(SignatureAlgorithm algorithm, String content, byte[] key, byte[] sign) {
        return Arrays.equals(signature(algorithm, content, key).bytes(), sign);
    }
}
