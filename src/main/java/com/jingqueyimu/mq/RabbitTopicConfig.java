package com.jingqueyimu.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主题模式队列配置
 *
 * @author zhuangyilian
 */
@Configuration
public class RabbitTopicConfig {
    
    public static final String TOPIC_QUEUE_1 = "topic.queue.1";
    public static final String TOPIC_QUEUE_2 = "topic.queue.2";
    public static final String TOPIC_QUEUE_ALL = "topic.queue.all";
    
    /**
     * amq.topic: 默认的广播模式交换机,指定其他名称将创建新的交换机
     */
    public static final String TOPIC_EXCHANGE = "amq.topic";
    
    /******************************* 创建队列 *******************************/
    
    @Bean
    public Queue topicQueue1() {
         return new Queue(TOPIC_QUEUE_1, true, false, false);
    }
    
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE_2, true, false, false);
    }
    
    @Bean
    public Queue topicQueueAll() {
        return new Queue(TOPIC_QUEUE_ALL, true, false, false);
    }
    
    /******************************* 创建交换机 *******************************/
    
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    
    /******************************* 绑定队列到交换机 *******************************/
    
    @Bean
    public Binding topicBinding1(TopicExchange topicExchange, Queue topicQueue1) {
        // 以指定 路由键 绑定队列 到交换机上
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with("topic.1");
    }
    
    @Bean
    public Binding topicBinding2(TopicExchange topicExchange, Queue topicQueue2) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("topic.2");
    }
    
    @Bean
    public Binding topicBindingAll(TopicExchange topicExchange, Queue topicQueueAll) {
        // 发送到topic.下的消息,topicQueueAll都会接收到
        return BindingBuilder.bind(topicQueueAll).to(topicExchange).with("topic.#");
    }
}
