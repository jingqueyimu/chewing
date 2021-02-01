package com.jingqueyimu.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 直接模式消息接收者
 *
 * @author zhuangyilian
 */
@Component
public class RabbitDirectReceiver {
    
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    // 监听指定队列
    @RabbitListener(queues = RabbitDirectConfig.DIRECT_QUEUE)
    public void process(String data) {
        log.debug("DirectReceiver: {}", data);
    }
}
