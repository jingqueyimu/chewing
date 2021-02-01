package com.jingqueyimu.service.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ消息队列服务
 *
 * @author zhuangyilian
 */
@Service
public class RabbitService {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AmqpTemplate rabbitTemplate;
    
    /**
     * 直接发送
     *
     * @param route
     * @param msg
     */
    public void sendDirect(String route, Object msg) {
        log.debug("DirectSender: {}", msg);
        // 默认交换机模式为direct,并且路由键为direct队列名
        rabbitTemplate.convertAndSend(route, msg);
    }
    
    /**
     * 通配符模式发送
     *
     * @param exchange
     * @param route
     * @param msg
     */
    public void sendTopic(String exchange, String route, Object msg) {
        log.debug("TopicSender: {}", msg);
        rabbitTemplate.convertAndSend(exchange, route, msg);
    }
    
    /**
     * 广播模式发送
     *
     * @param exchange
     * @param msg
     */
    public void sendFanout(String exchange, Object msg) {
        log.debug("FanoutSender: {}", msg);
        // 指定广播模式时,无论设置什么样的路由键都会被忽略
        rabbitTemplate.convertAndSend(exchange, "", msg);
    }
}
