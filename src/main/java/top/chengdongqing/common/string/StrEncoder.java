package top.chengdongqing.common.string;

/**
 * A string encode and decode utils
 *
 * @author Luyao
 */
public class StrEncoder {

    /**
     * Encode for string
     *
     * @param value the string to encode
     * @param type  the encoding type for the string
     * @return the encoded string by the encoding type
     */
    public static String encode(String value, StrEncodingType type) {
        return type.getEncodingLogic().apply(value);
    }

    /**
     * Decode for string
     *
     * @param value the string to decode
     * @param type  the decoding type for the string
     * @return the decoded string by the decoding type
     */
    public static String decode(String value, StrEncodingType type) {
        return type.getDecodingLogic().apply(value);
    }
}
