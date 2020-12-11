package top.chengdongqing.common.kit;

import java.util.HashMap;
import java.util.Map;

/**
 * A mini {@link HashMap}
 *
 * @author Luyao
 */
public class Kv<K, V> extends HashMap<K, V> {

    /**
     * Creates an instance of current class, and add the first key-value mapping
     *
     * @param key   the key of the mapping, type single
     * @param value the value of the mapping, type any
     * @param <K>   the type of all keys of current instance
     * @return the new instance with the key-value mapping
     */
    public static <K> Kv<K, Object> ofAny(K key, Object value) {
        return new Kv<K, Object>().add(key, value);
    }

    /**
     * Creates an instance of current class, and add the first key-value mapping
     *
     * @param key   the key of the mapping, type single
     * @param value the value of the mapping, type single
     * @param <K>   the type of all keys of current instance
     * @param <V>   the type of all values of current instance
     * @return the new instance of current class with the first key-value mapping
     */
    public static <K, V> Kv<K, V> of(K key, V value) {
        return new Kv<K, V>().add(key, value);
    }

    /**
     * Transforms the map to {@link Kv} instance
     *
     * @param paraMap the map to transform
     * @return the {@link Kv} instance of transformed
     */
    public static Kv<String, String> of(Map<String, String[]> paraMap) {
        Kv<String, String> params = new Kv<>();
        paraMap.forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.add(key, values[0]);
            }
        });
        return params;
    }

    /**
     * Appends a new key-value mapping to current {@link Kv} instance
     *
     * @param key   the key of the mapping
     * @param value the value of the mapping
     * @return current instance
     */
    public Kv<K, V> add(K key, V value) {
        put(key, value);
        return this;
    }

    /**
     * <p>Gets the value from current instance by the key</p>
     * <p>Will auto convert the value type by the type of variable to receive</p>
     *
     * @param key the key to match value
     * @param <T> the type of value
     * @return the value of the key-value mapping
     */
    public <T> T getAs(K key) {
        return (T) get(key);
    }

    /**
     * Transforms current {@link Kv} instance to a JSON string
     *
     * @return the JSON string
     */
    public String toJson() {
        return JsonKit.toJson(this);
    }
}
