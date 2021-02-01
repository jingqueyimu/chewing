package com.jingqueyimu.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限
 *
 * @author zhuangyilian
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Perm {
    
    /**
     * 分组(字典: PermissionGroup)
     */
    String group() default "";
    
    /**
     * 名称
     */
    String name() default "";
    
    /**
     * 描述
     */
    String description() default "";
}
