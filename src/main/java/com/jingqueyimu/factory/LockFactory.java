package com.jingqueyimu.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jingqueyimu.service.component.RedisService;

/**
 * 分布式锁工厂
 *
 * @author zhuangyilian
 */
@Component
public class LockFactory {

    @Autowired
    private RedisService redisService;
    
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
            if (redisService.setnx(key, key, releaseSecodes)) {
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
        redisService.delete(key);
    }
}
