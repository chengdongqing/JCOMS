package top.chengdongqing.common.cache;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存相关接口
 *
 * @author Luyao
 */
public interface CacheTemplate {

    /**
     * 默认缓存时长，单位：天
     */
    Integer DEFAULT_CACHE_DAYS = 7;

    /**
     * 加入缓存，使用默认缓存时长
     */
    void set(String key, String value);

    /**
     * 加入缓存
     */
    void set(String key, String value, Duration duration);

    /**
     * 从缓存中取字符串
     */
    String getStr(String key);

    /**
     * 从缓存中取列表
     */
    <T> List<T> getList(String key);

    /**
     * 从缓存中取指定map的指定键名的值
     */
    Object getHash(String key, String mapKey);

    /**
     * 从缓存中取map
     */
    <K, V> Map<K, V> getHash(String key);

    /**
     * 从缓存中取对象
     */
    Object get(String key);

    /**
     * 删除指定的缓存
     */
    void delete(String key);

    /**
     * 批量删除指定的缓存
     */
    void delete(Set<String> keys);

    /**
     * 是否存在指定的缓存
     */
    boolean exist(String key);

    /**
     * 清理所有缓存
     */
    void clear();
}
