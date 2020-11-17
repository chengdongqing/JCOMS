package top.chengdongqing.common.kit;

import java.util.LinkedHashMap;

/**
 * 有序的链式HashMap
 *
 * @author Luyao
 */
public class Lkv<K, V> extends LinkedHashMap<K, V> {

    /**
     * 创建实例
     *
     * @param key   键
     * @param value 值
     * @param <K>   键类型，自动根据第一个键的类型决定整体的键类型
     * @param <V>   值类型，自动根据第一个值的类型决定整体的值类型
     * @return Lkv实例
     */
    public static <K, V> Lkv<K, V> of(K key, V value) {
        return new Lkv<K, V>().add(key, value);
    }

    /**
     * 链式添加键值对
     *
     * @param key   键
     * @param value 值
     * @return 当前对象
     */
    public Lkv<K, V> add(K key, V value) {
        put(key, value);
        return this;
    }
}
