package top.chengdongqing.common.transformer;

import java.math.BigInteger;
import java.util.Base64;

/**
 * 字节数组转字符串
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
     * 转MD5签名16进制字符串
     * BigInteger会将签名串前面的0去除，导致长度不够
     * 这里填充0到签名串前面直到长度为32
     */
    public String toMD5Hex() {
        return fillMD5(toHex());
    }

    /**
     * 填充MD5的长度到32
     *
     * @param md5 原始MD5
     * @return 标准格式的MD5值
     */
    private String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }

    /**
     * 转base64字符串
     */
    public String toBase64() {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 转URL安全的base64字符串
     */
    public String toURLBase64() {
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    /**
     * 转普通文本
     */
    public String toText() {
        return new String(bytes);
    }
}
