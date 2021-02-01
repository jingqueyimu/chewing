package com.jingqueyimu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.jingqueyimu.factory.YamlPropertySourceFactory;

/**
 * 自定义配置
 * 将配置文件属性值,映射到组件中
 *
 * @author zhuangyilian
 */
@Component
@ConfigurationProperties(prefix="test")
@PropertySource(value="classpath:config/test2.yml", encoding="UTF-8", factory=YamlPropertySourceFactory.class)
public class TestYmlConfig {
    
    private String value;

    private Person person;
    
    private Dog dog;
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "TestYmlConfig [value=" + value + ", person=" + person + ", dog=" + dog + "]";
    }
}

class Person {
    
    private String name;
    
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + "]";
    }
}

class Dog {
    
    private String name;
    
    private int age;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Dog [name=" + name + ", age=" + age + "]";
    }
}