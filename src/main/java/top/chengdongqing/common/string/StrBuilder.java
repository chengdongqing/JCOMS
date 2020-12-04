package top.chengdongqing.common.string;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.kit.Kv;

import java.util.TreeMap;
import java.util.function.BiFunction;

/**
 * 字符串构建器
 *
 * @author Luyao
 */
public abstract class StrBuilder extends StrEncoder {

    /**
     * 构建查询字符串
     *
     * @param params 键值对
     * @return 查询字符串
     */
    public static String buildQueryStr(Kv<String, String> params) {
        return buildQueryStr(params, (StrEncodingType) null);
    }

    /**
     * 构建查询字符串
     *
     * @param params 键值对
     * @param type   编码类型
     * @return 查询字符串
     */
    public static String buildQueryStr(Kv<String, String> params, StrEncodingType type) {
        return buildQueryStr(params, type, (k, v) -> true);
    }

    /**
     * 构建查询字符串
     *
     * @param params    键值对
     * @param joinLogic 是否加入到查询字符串的判断逻辑
     * @return 查询字符串
     */
    public static String buildQueryStr(Kv<String, String> params, BiFunction<String, String, Boolean> joinLogic) {
        return buildQueryStr(params, null, joinLogic);
    }


    /**
     * 构建键值对形式的查询字符串
     *
     * @param params    键值对
     * @param type      编码类型
     * @param joinLogic 是否加入到查询字符串的判断逻辑
     * @return 查询字符串
     */
    public static String buildQueryStr(Kv<String, String> params, StrEncodingType type, BiFunction<String, String, Boolean> joinLogic) {
        StringBuilder str = new StringBuilder();
        new TreeMap<>(params).forEach((k, v) -> {
            if (StringUtils.isNoneBlank(k, v) && joinLogic.apply(k, v)) {
                str.append(k).append("=").append(type == null ? v : encode(v, type)).append("&");
            }
        });
        return str.substring(0, str.length() - 1);
    }
}
