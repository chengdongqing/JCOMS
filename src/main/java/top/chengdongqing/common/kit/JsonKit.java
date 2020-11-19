package top.chengdongqing.common.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.util.List;

/**
 * JSON处理工具类
 * 基于Google Gson
 *
 * @author Luyao
 */
public class JsonKit {

    /**
     * 对象转JSON字符串
     *
     * @param data 需要转换的数据
     * @return JSON字符串
     */
    public static String toJson(Object data) {
        return JSON.toJSONString(data);
    }

    /**
     * 对象转JSON字节数组
     *
     * @param data 需要转换的数据
     * @return JSON字节数组
     */
    public static byte[] toJsonBytes(Object data) {
        return JSON.toJSONBytes(data);
    }

    /**
     * 对象转JSON字符串
     * 属性名由驼峰转下划线形式
     * 仅支持对象属性名，不支持Map键名
     *
     * @param data 需要转换的数据
     * @return JSON字符串
     */
    public static String toJsonWithUnderscore(Object data) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.setPropertyNamingStrategy(PropertyNamingStrategy.SnakeCase);
        return JSON.toJSONString(data, serializeConfig);
    }

    /**
     * 解析JSON对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @return Java对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 解析JSON数组
     *
     * @param json  JSON字符串
     * @param clazz 集合类型
     * @return Java集合
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 解析JSON对象
     *
     * @param json  JSON字节数组
     * @param clazz 对象类型
     * @return Java对象
     */
    public static <T> T parseObject(byte[] json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 解析JSON为Kv
     *
     * @param json JSON字符串
     * @param <K>  键类型
     * @param <V>  值类型
     * @return Kv对象
     */
    public static <K, V> Kv<K, V> parseKv(String json) {
        return JSON.parseObject(json, Kv.class);
    }

    /**
     * 解析JSON为Kv
     *
     * @param json JSON字节数组
     * @param <K>  键类型
     * @param <V>  值类型
     * @return Kv对象
     */
    public static <K, V> Kv<K, V> parseKv(byte[] json) {
        return JSON.parseObject(json, Kv.class);
    }
}
