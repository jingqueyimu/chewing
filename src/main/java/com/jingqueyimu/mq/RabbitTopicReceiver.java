package com.jingqueyimu.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 主题模式消息接收者
 *
 * @author zhuangyilian
 */
@Component
public class RabbitTopicReceiver {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @RabbitListener(queues = RabbitTopicConfig.TOPIC_QUEUE_1)
    public void process1(String data) {
        log.debug("TopicReceiver1: {}", data);
    }
    
    @RabbitListener(queues = RabbitTopicConfig.TOPIC_QUEUE_2)
    public void process2(String data) {
        log.debug("TopicReceiver2: {}", data);
    }
    
    @RabbitListener(queues = RabbitTopicConfig.TOPIC_QUEUE_ALL)
    public void processAll(String data) {
        log.debug("TopicReceiverAll: {}", data);
    }
}
