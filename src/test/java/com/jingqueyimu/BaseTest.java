package com.jingqueyimu;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jingqueyimu.MyApplication;

/**
 * Junit基础测试类
 * 其他测试类只需继承该基础测试类即可,不用在每个测试类上加注解
 * 注意src/test/java中不要注入src/main/java中的类,否则运行时会找不到类
 *
 * @author zhuangyilian
 */
// 指定测试运行器为SpringJUnit4ClassRunner,从而自动创建Spring运行环境
@RunWith(SpringJUnit4ClassRunner.class)
// 指定SpringBoot工程的Application启动类(会测试开始前启动SpringBoot项目)
@SpringBootTest(classes=MyApplication.class)
// 对于Web项目,Junit需要模拟ServletContext,因此需要在测试类加上@WebAppConfiguration(如果不测试Web相关的可以不加)
@WebAppConfiguration
public class BaseTest {
    
}