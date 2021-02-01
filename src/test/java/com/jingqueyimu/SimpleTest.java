package com.jingqueyimu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 简单测试
 *
 * @author zhuangyilian
 */
public class SimpleTest extends BaseTest {
    
    @Before
    public void testBefore() {
        System.out.println("junit before...");
    }
    
    @After
    public void testAfter() {
        System.out.println("junit after...");
    }
    
    @Test
    public void test() {
        System.out.println("junit test...");
    }
}
