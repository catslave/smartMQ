package com.rekchina.rocketmq.client;

import com.rekchina.rocketmq.client.producer.DefaultMQProducer;
import com.rekchina.rocketmq.netty.Message;
import org.junit.Test;

/**
 * Producer 测试类
 * Created by Administrator on 2016/8/19.
 */
public class ProducerProTest {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("Producer");
        producer.start();//要在线程里面启动，否则会同步等待，后面的代码没办法执行

        Message message = new Message("Topic-a", "hello broker".getBytes());
        producer.send(message);
    }
}
