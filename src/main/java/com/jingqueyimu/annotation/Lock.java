package com.jingqueyimu.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁
 *
 * @author zhuangyilian
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    
    /**
     * 锁的key
     */
    String[] keys() default {};
    
    /**
     * 尝试加锁等待时间
     */
    long waitTime() default 0;
    
    /**
     * 加锁后自动释放时间
     */
    long releaseTime() default 0;
    
    /**
     * 尝试加锁失败后提示信息
     */
    String msg() default "您这手速有点快啊，服务器都承受不了！";
}
