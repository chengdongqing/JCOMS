package top.chengdongqing.common.signature;

import java.math.BigInteger;
import java.util.Base64;

/**
 * 将字符串转字节数组
 *
 * @author Luyao
 */
public record ToByteArray(String str) {

    /**
     * 静态工厂方法
     */
    public static ToByteArray of(String str) {
        return new ToByteArray(str);
    }

    /**
     * 从16进制字符串转字节数组
     */
    public byte[] fromHex() {
        return new BigInteger(str, 16).toByteArray();
    }

    /**
     * 从base64字符串解码为字节数组
     */
    public byte[] fromBase64() {
        return Base64.getDecoder().decode(str);
    }
}
