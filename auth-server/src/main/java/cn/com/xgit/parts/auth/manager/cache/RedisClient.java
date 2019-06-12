package cn.com.xgit.parts.auth.manager.cache;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisClient {

    @Resource(name = "redisTemplate")
    protected RedisTemplate redisTemplate;

    // 一个消失
    private Long defaultCacheTime = 3600L;

    /**
     * 根据key获取value
     */
    @SuppressWarnings("unchecked")
    public <V> V get(String key) {
        return (V) redisTemplate.opsForValue().get(key);
    }

    @SuppressWarnings("unchecked")
    public <HK, HV> Map<HK, HV> getHashAll(String key) {
        Map<HK, HV> entries = (Map<HK, HV>) redisTemplate.opsForHash().entries(key);
        return entries;
    }

    /**
     * 根据key列表批量获取value
     */
    @SuppressWarnings("unchecked")
    public <V> List<V> multiGet(List<String> keys) {
        return (List<V>) redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 根据key设置value 过期时间单位:秒
     */
    public void set(String key, Object value, long timeout) {
        if (timeout <= 0) {
            log.error("缓存失效时间必须大于0");
            return;
        }
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 根据key设置value
     */
    public void set(String key, Object value) {
        set(key, value, defaultCacheTime);
    }

    /**
     * 列表右添加
     *
     * @param key
     * @param values
     */
    public void rightPushAll(String key, Collection<?> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 删除指定key, 区分环境
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean expire(String key, int second) {
        return redisTemplate.expire(key, (long) second, TimeUnit.SECONDS).booleanValue();
    }

    /**
     * 设置key过期时间
     */
    public void expire(String key, Long value, TimeUnit timeUnit) {
        redisTemplate.expire(key, value, timeUnit);
    }

    /**
     * 如果缓存中不存在改key，则添加
     *
     * @return true-设置成功
     */
    public boolean setnx(String key, Object value) {
        boolean result = false;
        try {
            result = this.redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("setnx error, key: {}, value: {}", key, value, e);
        }
        return result;
    }

    /**
     * @param second 设置的key超时过期清楚
     * @return true-设置成功
     */
    public boolean setnx(String key, Object value, long second) {
        boolean result = false;
        try {
            result = this.redisTemplate.opsForValue().setIfAbsent(key, value);
            if (result) {
                redisTemplate.expire(key, second, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("setnx error, key: {}, value: {}", key, value, e);
        }
        return result;
    }

    /**
     * 自增,有效期只会首次设置 timeout为0则永久有效
     */
    public Long increment4Long(String key, long timeout) {
        Long value = redisTemplate.opsForValue().increment(key, 1);
        if (timeout > 0 && value != null && value.intValue() == 1) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
        return value;
    }

    public Long increment4LongOriginKey(String key, long timeout) {
        Long value = redisTemplate.opsForValue().increment(key, 1);
        if (timeout > 0 && value != null && value.intValue() == 1) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
        return value;
    }


    /**
     * hash数据类型,添加
     */
    public <K> void setHash(String key, K hashKey, Object value, long timeout) {
        if (key == null || hashKey == null || value == null) {
            return;
        }
        redisTemplate.opsForHash().put(key, hashKey, value);
        if (timeout != 0) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * hash数据类型,添加(默认有效期)
     */
    public <K> void setHash(String key, K hashKey, Object value) {
        setHash(key, hashKey, value, defaultCacheTime);
    }

    /**
     * hash数据类型, 获取
     */
    @SuppressWarnings("unchecked")
    public <K, V> V getHash(String key, K hashKey) {
        return (V) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * hash数据类型,批量添加
     *
     * @param <K>
     * @param key
     * @param value
     */
    public <K, V> void setMultiHash(String key, Map<K, V> value) {
        if (key == null || MapUtils.isEmpty(value)) {
            return;
        }
        redisTemplate.opsForHash().putAll(key, value);
    }

    @SuppressWarnings("unchecked")
    public <K, V> List<V> getHashMulti(String key, List<K> hashKeyList) {
        if (CollectionUtils.isEmpty(hashKeyList)) {
            return Lists.newArrayList();
        }
        List<Object> ret = redisTemplate.opsForHash().multiGet(key, transList(hashKeyList));
        if (CollectionUtils.isEmpty(ret)) {
            return Lists.newArrayList();
        }
        Iterator<Object> i = ret.iterator();
        while (i.hasNext()) {
            if (i.next() == null) {
                i.remove();
            }
        }
        return (List<V>) ret;
    }

    public <K> void delHash(String key, K hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public <K> void delHashMulti(String key, List<K> hashKeyList) {
        redisTemplate.opsForHash().delete(key, hashKeyList.toArray());
    }

    /**
     * 字符串list转Object list
     */
    private <T> List<Object> transList(List<T> stringList) {
        List<Object> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(stringList)) {
            return list;
        }
        for (T e : stringList) {
            list.add(e);
        }
        return list;
    }

}
