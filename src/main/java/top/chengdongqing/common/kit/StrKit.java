package top.chengdongqing.common.kit;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
     * 构建键值对参数字符串
     */
    public static String buildQueryStr(Map<String, String> params, boolean popEncode) {
        // 将map转为有序的treemap
        Map<String, String> sortedParams = new TreeMap<>(params);

        // 所有参数的字符串集合
        StringBuilder sb = new StringBuilder();
        sortedParams.forEach((key, value) -> {
            if (StringUtils.isNotBlank(value) && !key.equals("key") && !key.equals("sign")) {
                sb.append(key).append("=").append(popEncode ? popUrlEncode(value) : value).append("&");
            }
        });
        // 将key放在最后，仅微信支付
        if (params.containsKey("key")) {
            sb.append("key").append("=").append(params.get("key")).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 符合POP规则的特殊编码
     */
    private static String popUrlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("*", "%2A")
                .replace("%7E", "~");
    }
}
