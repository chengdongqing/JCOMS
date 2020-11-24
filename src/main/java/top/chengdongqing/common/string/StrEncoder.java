package top.chengdongqing.common.string;

/**
 * 字符串编解码器
 *
 * @author Luyao
 */
public abstract class StrEncoder {

    /**
     * 字符串编码
     *
     * @param value 待编码的字符串
     * @param type  编码类型
     * @return 编码后的字符串
     */
    public static String encode(String value, StrEncodingType type) {
        return type.getEncodeLogic().apply(value);
    }

    /**
     * 字符串解码
     *
     * @param value 待解码的字符串
     * @param type  编码类型
     * @return 解码后的字符串
     */
    public static String decode(String value, StrEncodingType type) {
        return type.getDecodeLogic().apply(value);
    }
}
