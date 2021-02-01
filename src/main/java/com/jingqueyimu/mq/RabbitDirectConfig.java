package com.jingqueyimu.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 直接模式队列配置
 *
 * @author zhuangyilian
 */
@Configuration
public class RabbitDirectConfig {
    
    /**
     * 队列名称
     */
    public static final String DIRECT_QUEUE = "direct.queue";
    
    /**
     * 交换机名称(amq.direct: 默认的直接模式交换机,指定其他名称将创建新的交换机)
     */
    public static final String DIRECT_EXCHANGE = "amq.direct";
    
    /******************************* 创建队列 *******************************/
    
    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE, true, false, false);
    }
    
    /******************************* 创建交换机 *******************************/
    
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }
    
    /******************************* 绑定队列到交换机 *******************************/
    
    @Bean
    public Binding directBinding(DirectExchange directExchange, Queue directQueue) {
        return BindingBuilder.bind(directQueue).to(directExchange).with(DIRECT_QUEUE);
    }
}
