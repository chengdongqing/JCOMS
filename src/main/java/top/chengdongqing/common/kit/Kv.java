package top.chengdongqing.common.kit;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * 链式HashMap
 *
 * @author Luyao
 */
public class Kv<K, V> extends HashMap<K, V> {

    /**
     * 创建实例
     * key、value均为任意类型
     *
     * @return Kv实例
     */
    public static Kv<Object, Object> of() {
        return new Kv<>();
    }

    /**
     * 创建实例
     *
     * @param key   键
     * @param value 值
     * @param <K>   键类型，根据第一个键的数据类型自动决定整体的键类型
     * @param <V>   值类型，根据第一个值的数据类型自动决定整体的值类型
     * @return Kv实例
     */
    public static <K, V> Kv<K, V> of(K key, V value) {
        return new Kv<K, V>().add(key, value);
    }

    /**
     * 链式添加键值对
     *
     * @param key   键
     * @param value 值
     * @return 当前对象
     */
    public Kv<K, V> add(K key, V value) {
        put(key, value);
        return this;
    }

    /**
     * 获取值
     * 自动根据接收变量定义的类型进行强转
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值
     */
    public <T> T getAs(K key) {
        return (T) get(key);
    }

    /**
     * 将当前对象转JSON字符串
     *
     * @return 当前对象JSON
     */
    public String toJson() {
        return JSON.toJSONString(this);
    }
}
