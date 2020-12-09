package top.chengdongqing.common.string;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.StrKit;

import java.util.TreeMap;
import java.util.function.BiFunction;

/**
 * A string build utils
 *
 * @author Luyao
 */
public class StrBuilder extends StrEncoder {

    public static String buildQueryStr(Kv<String, String> params) {
        return buildQueryStr(params, (StrEncodingType) null);
    }

    public static String buildQueryStr(Kv<String, String> params, StrEncodingType type) {
        return buildQueryStr(params, type, (k, v) -> true);
    }

    public static String buildQueryStr(Kv<String, String> params, BiFunction<String, String, Boolean> joinLogic) {
        return buildQueryStr(params, null, joinLogic);
    }


    /**
     * Builds key-value URL query string
     *
     * @param params    the key-value mappings to build
     * @param type      the encoding type for value of key-value
     * @param joinLogic the logic for join to the query string
     * @return the built URL query string
     */
    public static String buildQueryStr(Kv<String, String> params, StrEncodingType type, BiFunction<String, String, Boolean> joinLogic) {
        StringBuilder str = new StringBuilder();
        new TreeMap<>(params).forEach((k, v) -> {
            if (StrKit.isNoneBlank(k, v) && joinLogic.apply(k, v)) {
                str.append(k).append("=").append(type == null ? v : encode(v, type)).append("&");
            }
        });
        return str.substring(0, str.length() - 1);
    }
}
