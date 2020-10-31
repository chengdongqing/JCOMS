package top.chengdongqing.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis缓存操作模板
 *
 * @author Luyao
 */
@Component
public class RedisCacheTemplate implements CacheTemplate {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, String value) {
        set(key, value, Duration.ofDays(DEFAULT_CACHE_DAYS));
    }

    @Override
    public void set(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public String getStr(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    @Override
    public List getList(String key) {
        Object values = redisTemplate.opsForValue().get(key);
        return values != null ? (List) values : null;
    }

    @Override
    public Object getHash(String key, String mapKey) {
        Object values = redisTemplate.opsForValue().get(key);
        return values != null ? ((Map) values).get(mapKey) : null;
    }

    @Override
    public Map getHash(String key) {
        Object values = redisTemplate.opsForValue().get(key);
        return values != null ? (Map) values : null;
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void clear() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}
