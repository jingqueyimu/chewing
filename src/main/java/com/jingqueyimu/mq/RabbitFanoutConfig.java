package com.jingqueyimu.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 广播模式队列配置
 *
 * @author zhuangyilian
 */
@Configuration
public class RabbitFanoutConfig {
    
    public static final String FANOUT_QUEUE_1 = "fanout.queue.1";
    public static final String FANOUT_QUEUE_2 = "fanout.queue.2";
    public static final String FANOUT_QUEUE_3 = "fanout.queue.3";
    
    /**
     * amq.fanout: 默认的广播模式交换机，指定其他名称将创建新的交换机
     */
    public static final String FANOUT_EXCHANGE = "amq.fanout";
    
    /******************************* 创建队列 *******************************/
    
    @Bean
    public Queue fanoutQueue1() {
         return new Queue(FANOUT_QUEUE_1, true, false, false);
    }
    
    @Bean
    public Queue fanoutQueue2() {
        return new Queue(FANOUT_QUEUE_2, true, false, false);
    }
    
    @Bean
    public Queue fanoutQueue3() {
        return new Queue(FANOUT_QUEUE_3, true, false, false);
    }
    
    /******************************* 创建交换机 *******************************/
    
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    
    /******************************* 绑定队列到交换机 *******************************/
    
    @Bean
    public Binding fanoutBinding1(FanoutExchange fanoutExchange, Queue fanoutQueue1) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }
    
    @Bean
    public Binding fanoutBinding2(FanoutExchange fanoutExchange, Queue fanoutQueue2) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
    
    @Bean
    public Binding fanoutBinding3(FanoutExchange fanoutExchange, Queue fanoutQueue3) {
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);
    }
}
