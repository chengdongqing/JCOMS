package top.chengdongqing.common.signature;

import java.math.BigInteger;
import java.util.Base64;

/**
 * 字节数组
 *
 * @author Luyao
 */
public record ByteArray(byte[] bytes) {

    /**
     * 静态工厂方法
     */
    public static ByteArray of(byte[] bytes) {
        return new ByteArray(bytes);
    }

    /**
     * 将签名转16进制数据
     */
    public String toHex() {
        return new BigInteger(1, bytes).toString(16);
    }

    /**
     * 将签名通过base64编码
     */
    public String toBase64() {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
