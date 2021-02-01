package com.jingqueyimu.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 广播模式消息接收者
 *
 * @author zhuangyilian
 */
@Component
public class RabbitFanoutReceiver {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @RabbitListener(queues = RabbitFanoutConfig.FANOUT_QUEUE_1)
    public void process1(String data) {
        log.debug("FanoutReceiver1: {}", data);
    }
    
    @RabbitListener(queues = RabbitFanoutConfig.FANOUT_QUEUE_2)
    public void process2(String data) {
        log.debug("FanoutReceiver2: {}", data);
    }
    
    @RabbitListener(queues = RabbitFanoutConfig.FANOUT_QUEUE_3)
    public void process3(String data) {
        log.debug("FanoutReceiver3: {}", data);
    }
}
