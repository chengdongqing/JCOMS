package top.chengdongqing.common.kit;

import top.chengdongqing.common.string.StrBuilder;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 字符串工具类
 *
 * @author Luyao
 */
public class StrKit extends StrBuilder {

    /**
     * 获取UUID
     *
     * @return 去除横杠后的UUID
     */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成6位随机数字
     *
     * @return 6位随机数字
     */
    public static String generate6Digits() {
        return ThreadLocalRandom.current().nextInt(100000, 999999) + "";
    }
}
