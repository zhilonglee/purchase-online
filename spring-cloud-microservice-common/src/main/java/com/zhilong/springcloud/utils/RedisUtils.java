package com.zhilong.springcloud.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    public static final TimeUnit TIME_TO_HOURS = TimeUnit.HOURS;
    public static final TimeUnit TIME_TO_MINUTES = TimeUnit.MINUTES;
    public static final TimeUnit TIME_TO_SECONDS = TimeUnit.SECONDS;
    public static final TimeUnit TIME_TO_MILLISECONDS = TimeUnit.MILLISECONDS;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisUtils(StringRedisTemplate stringRedisTemplate, RedisTemplate<Object, Object> redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Get the String by specific key
     *
     * @param key
     * @return
     */
    public String getStr(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * Set the String by specific key
     * @param key
     * @param val
     */
    public void setStr(String key, String val){
        stringRedisTemplate.opsForValue().set(key,val);
    }

    /**
     * Delete the caching by specific key
     * @param key
     */
    public Boolean del(String key){
        return stringRedisTemplate.delete(key);
    }

    /**
     * Get the Object by specific object key
     * @param objKey
     * @return
     */
    public Object getObj(Object objKey){
        return redisTemplate.opsForValue().get(objKey);
    }

    /**
     * Set the Object by specific object key
     * @param objKey
     * @param objVal
     */
    public void setObj(Object objKey, Object objVal){
        redisTemplate.opsForValue().set(objKey,objVal);
    }

    /**
     * Delete the caching by specific objectkey
     * @param objKey
     */
    public Boolean del(Object objKey){
        return redisTemplate.delete(objKey);
    }


    /**
     * Expire the caching by specific Key
     *
     * @param key
     * @param timeout
     * @param unit {@link TIME_TO_HOURS}{@link TIME_TO_MINUTES}{@link TIME_TO_SECONDS}{@link TIME_TO_MILLISECONDS}
     */
    public void expire(Object key,final long timeout, final TimeUnit unit){
        redisTemplate.expire(key,timeout,unit);
    }

    /**
     * Get remaining expiring time(TTL)
     * @param key
     * @return
     */
    public Long getExpire(Object key) { return redisTemplate.getExpire(key); }

    /**
     * Get Keys Set using pattern
     *
     * @param pattern
     * @return Set<Object>
     */
    public Set<Object> keys(Object pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        return stringRedisTemplate.keys(pattern);
    }
    /**
     * Watch key
     *
     * @param key
     */
    public void watch(Object key){
        redisTemplate.watch(key);
    }
}
