package top.chengdongqing.common.signature.transform;

import java.math.BigInteger;
import java.util.Base64;

/**
 * @author Luyao
 */
public record StrToBytes(String str) {

    /**
     * 静态工厂方法
     */
    public static StrToBytes of(String str) {
        return new StrToBytes(str);
    }

    /**
     * 从16进制字符串转字节数组
     */
    public byte[] toBytesFromHex() {
        return new BigInteger(str, 16).toByteArray();
    }

    /**
     * 从base64字符串转字节数组
     */
    public byte[] toBytesFromBase64() {
        return Base64.getDecoder().decode(str);
    }
}
