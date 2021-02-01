package com.jingqueyimu;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.mq.RabbitDirectConfig;
import com.jingqueyimu.mq.RabbitFanoutConfig;
import com.jingqueyimu.mq.RabbitTopicConfig;
import com.jingqueyimu.service.component.RabbitService;

/**
 * RabbitMQ测试
 *
 * @author zhuangyilian
 */
public class RabbitTest extends BaseTest {
    
    @Autowired
    private RabbitService rabbitService;
    
    @Test
    public void testSendDirect() {
        rabbitService.sendDirect(RabbitDirectConfig.DIRECT_QUEUE, "test direct...");
    }
    
    @Test
    public void testSendDirectObj() {
        JSONObject data = new JSONObject();
        data.put("content", "test direct obj...");
        // 发送对象
        rabbitService.sendDirect(RabbitDirectConfig.DIRECT_QUEUE, data);
    }
    
    @Test
    public void testSendTopic() {
        rabbitService.sendTopic(RabbitTopicConfig.TOPIC_EXCHANGE, "topic.1", "test topic 1...");
//        rabbitService.sendTopic(RabbitTopicConfig.TOPIC_EXCHANGE, "topic.2", "test topic 2...");
    }
    
    @Test
    public void testSendFanout() {
        rabbitService.sendFanout(RabbitFanoutConfig.FANOUT_EXCHANGE, "test fanout...");
    }
}
