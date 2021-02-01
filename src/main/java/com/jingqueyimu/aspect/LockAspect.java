package com.jingqueyimu.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.annotation.Lock;
import com.jingqueyimu.constant.StatusCode;
import com.jingqueyimu.exception.AppException;
import com.jingqueyimu.service.component.RedisService;

/**
 * 锁切面
 *
 * @author zhuangyilian
 */
@Aspect
@Component
public class LockAspect {
    
    @Autowired
    private RedisService redisService;

    /**
     * 环绕通知
     *
     * @param point
     * @param lock
     * @throws Throwable
     */
    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint point, Lock lock) throws Throwable {
        // 锁的key
        String lockKey = getLockKey(point, lock);
        try {
            // 尝试加锁
            if (!redisService.tryLock(lockKey, lock.waitTime(), lock.releaseTime())) {
                throw new AppException(StatusCode.ERR_SYS_LOCK, lock.msg());
            }
            // 执行连接点处的操作
            return point.proceed();
        } finally {
            // 释放锁
            redisService.releaseLock(lockKey);
        }
    }
    
    /**
     * 获取锁的key
     *
     * @param point
     * @param lock
     * @return
     */
    private String getLockKey(ProceedingJoinPoint point, Lock lock) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 锁的key
        StringBuffer lockKey = new StringBuffer(signature.getDeclaringTypeName()).append(".").append(signature.getMethod().getName());
        if (lock.keys().length <= 0) {
           return lockKey.toString(); 
        }
        // 方法参数名
        String[] parameterNames = signature.getParameterNames();
        // 方法参数值
        Object[] args = point.getArgs();
        // 组装参数信息
        Map<String, String> paramMap = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            try {
                JSONObject json = (JSONObject)JSONObject.toJSON(args[i]);
                for (String key : json.keySet()) {
                    paramMap.put(parameterNames[i].concat(".").concat(key), json.getString(key));
                }
            } catch (Exception e) {
                paramMap.put(parameterNames[i], args[i] == null ? "" : args[i].toString());
            }
        }
        StringBuffer paramKey = new StringBuffer();
        for (int i = 0; i < lock.keys().length; i++) {
            paramKey.append(":").append(paramMap.get(lock.keys()[i]));
        }
        return lockKey.append(".").append(paramKey).toString();
    }
}
