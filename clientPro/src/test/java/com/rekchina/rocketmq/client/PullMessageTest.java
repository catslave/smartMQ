package com.rekchina.rocketmq.client;

import com.rekchina.rocketmq.client.consumer.DefaultMQConsumer;
import org.junit.Test;

/**
 * Test Pull Message
 * Created by Administrator on 2016/9/1.
 */
public class PullMessageTest {

    public static void main(String[] args) {
        DefaultMQConsumer consumer = new DefaultMQConsumer("consumer");
        consumer.start();
        consumer.subscribe("Topic-a");
        consumer.pull();
    }

}
