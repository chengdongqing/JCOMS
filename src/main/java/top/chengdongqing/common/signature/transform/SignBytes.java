package top.chengdongqing.common.signature.transform;

import java.math.BigInteger;
import java.util.Base64;

/**
 * @author Luyao
 */
public record SignBytes(byte[] bytes) {

    /**
     * 静态工厂方法
     */
    public static SignBytes of(byte[] bytes) {
        return new SignBytes(bytes);
    }

    /**
     * 将签名转16进制字符串
     */
    public String toHex() {
        return new BigInteger(1, bytes).toString(16);
    }

    /**
     * 将签名转base64字符串
     */
    public String toBase64() {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
