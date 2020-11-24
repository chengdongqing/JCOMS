package top.chengdongqing.common.kit;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.constant.StrEncodingType;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

/**
 * 字符串工具类
 *
 * @author Luyao
 */
public class StrKit {

    /**
     * 获取UUID
     *
     * @return 去除横杠后的UUID
     */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成随机代码
     */
    public static String generateRandomCode() {
        return ThreadLocalRandom.current().nextInt(100000, 999999) + "";
    }

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

    /**
     * 构建查询字符串
     *
     * @param params 键值对
     * @return 查询字符串
     */
    public static String buildQueryStr(Kv<String, String> params) {
        return buildQueryStr(params, null);
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
     * 构建键值对形式的查询字符串
     *
     * @param params    键值对
     * @param type      编码类型
     * @param joinLogic 是否加入到查询字符串的判断逻辑
     * @return 查询字符串
     */
    public static String buildQueryStr(Kv<String, String> params, StrEncodingType type, BiFunction<String, String, Boolean> joinLogic) {
        // 转为有序的map
        Map<String, String> sortedParams = new TreeMap<>(params);
        // 构建键值对字符串
        StringBuilder str = new StringBuilder();
        sortedParams.forEach((k, v) -> {
            if (StringUtils.isNoneBlank(k, v) && joinLogic.apply(k, v)) {
                str.append(k).append("=").append(type == null ? v : encode(v, type)).append("&");
            }
        });
        // 去除最后的&符合
        return str.substring(0, str.length() - 1);
    }
}
