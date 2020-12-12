package top.chengdongqing.common.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.util.List;

/**
 * JSON utility methods
 *
 * @author Luyao
 */
public class JsonKit {

    /**
     * Transforms the object to JSON string
     *
     * @param data the object to transform
     * @return the JSON string
     */
    public static String toJson(Object data) {
        return JSON.toJSONString(data);
    }

    /**
     * Transforms the object to JSON byte array
     *
     * @param data the object to transform
     * @return the JSON byte array
     */
    public static byte[] toJsonBytes(Object data) {
        return JSON.toJSONBytes(data);
    }

    /**
     * <p>Transforms the object to JSON string</p>
     * <p>The object field name will be auto underscored</p>
     * <p>Not support {@code Map} field name</p>
     *
     * @param data the object to transform
     * @return the JSON string
     */
    public static String toJsonWithUnderscore(Object data) {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.setPropertyNamingStrategy(PropertyNamingStrategy.SnakeCase);
        return JSON.toJSONString(data, serializeConfig);
    }

    /**
     * Parses the JSON string into an object of a given type
     *
     * @param json  the JSON string to transform
     * @param clazz the class of the object
     * @return the Java object
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * Parses the JSON string to a JAVA list of a given type
     *
     * @param json  the JSON string to transform
     * @param clazz the class of item of the list
     * @return the Java list
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * Parses the JSON byte array to a JAVA object of a given type
     *
     * @param json  the JSON byte array
     * @param clazz the class of the object
     * @return the Java object
     */
    public static <T> T parseObject(byte[] json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * Parses the JSON string to {@link Kv} object
     *
     * @param json the JSON string
     * @param <K>  the type of the key
     * @param <V>  the type of the value
     * @return the {@code Kv} object
     */
    public static <K, V> Kv<K, V> parseKv(String json) {
        return JSON.parseObject(json, Kv.class);
    }

    /**
     * Parses the JSON byte array to {@link Kv} object
     *
     * @param json the JSON byte array
     * @param <K>  the type of the key
     * @param <V>  the type of the value
     * @return the {@code Kv} object
     */
    public static <K, V> Kv<K, V> parseKv(byte[] json) {
        return JSON.parseObject(json, Kv.class);
    }
}
