package top.chengdongqing.common.transformer;

import java.math.BigInteger;
import java.util.Base64;

/**
 * 字节数据转字符串
 *
 * @author Luyao
 */
public record BytesToStr(byte[] bytes) {

    /**
     * 静态工厂方法
     */
    public static BytesToStr of(byte[] bytes) {
        return new BytesToStr(bytes);
    }

    /**
     * 转16进制字符串
     */
    public String toHex() {
        return new BigInteger(1, bytes).toString(16);
    }

    /**
     * 转base64字符串
     */
    public String toBase64() {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 转普通文本
     */
    public String toText() {
        return new String(bytes);
    }
}
