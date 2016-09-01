package com.rekchina.rocketmq.client;

import com.rekchina.rocketmq.client.producer.DefaultMQProducer;
import org.junit.Test;

/**
 * Producer 测试类
 * Created by Administrator on 2016/8/19.
 */
public class ProducerProTest {

    @Test
    public void producerTest() {
        DefaultMQProducer producer = new DefaultMQProducer("Producer");
        producer.start();//要在线程里面启动，否则会同步等待，后面的代码没办法执行
        producer.send("hello broker");
    }
}
