package com.jingqueyimu.service.component;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * Redis缓存服务
 *
 * @author zhuangyilian
 */
@Service
public class RedisService {
    
    // 当指定<K,V>时,不能用@Autowired,不然将注入SpringBoot自动配置的RedisTemplate
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.boundValueOps(key).set(value);
    }
    
    /**
     * 设置缓存及过期时间
     *
     * @param key
     * @param value
     * @param seconds
     */
    public void set(String key, Object value, long seconds) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        // 原子性操作
        ops.set(key, value, seconds, TimeUnit.SECONDS);
    }
    
    /**
     * 如果key不存在,则设置缓存
     *
     * @param key
     * @param value
     */
    public boolean setnx(String key, Object value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.setIfAbsent(key, value);
    }
    
    /**
     * 如果key不存在,设置缓存及过期时间
     *
     * @param key
     * @param value
     * @param seconds
     */
    public boolean setnx(String key, Object value, long seconds) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
    }
    
    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.boundValueOps(key).get();
    }
    
    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }
    
    /**
     * 设置缓存过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(String key, long seconds) {
        redisTemplate.boundValueOps(key).expire(seconds, TimeUnit.SECONDS);
    }
    
    /**
     * 是否存在指定key
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 尝试加锁
     *
     * @param key
     * @param waitSeconds
     * @param releaseSecodes
     * @return
     */
    public boolean tryLock(String key, long waitSeconds, long releaseSecodes) {
        // 默认自动释放锁的时间: 60s
        if (releaseSecodes <= 0) {
            releaseSecodes = 60;
        }
        long startTime = System.currentTimeMillis();
        while (true) {
            // value可以是任意值
            if (setnx(key, key, releaseSecodes)) {
                return true;
            }
            // 超过等待时间,则加锁失败
            if (System.currentTimeMillis() - startTime > waitSeconds * 1000) {
                return false;
            }
        }
    }

    /**
     * 释放锁
     *
     * @param key
     */
    public void releaseLock(String key) {
        delete(key);
    }
}
