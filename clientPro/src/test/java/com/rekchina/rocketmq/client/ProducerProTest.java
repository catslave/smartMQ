package com.rekchina.rocketmq.client;

import com.rekchina.rocketmq.client.consumer.DefaultMQConsumer;
import com.rekchina.rocketmq.client.producer.DefaultMQProducer;
import com.rekchina.rocketmq.netty.Message;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Producer 测试类
 * Created by Administrator on 2016/8/19.
 */
public class ProducerProTest {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("Producer");
        producer.start();//要在线程里面启动，否则会同步等待，后面的代码没办法执行

        ExecutorService executorService = Executors.newFixedThreadPool(10000);

        for(int i = 0; i < 100000; i++) {
            executorService.execute(new MessageService(producer, i));
        }

    }


}

class MessageService implements Runnable {

    private DefaultMQProducer producer;
    private int i;

    public MessageService(DefaultMQProducer producer, int i) {
        this.producer = producer;
        this.i = i;
    }

    @Override
    public void run() {
        Message message = new Message("Topic-a", ("hello broker " + this.i).getBytes());
        producer.send(message);
        System.out.println("send message " + this.i);
    }
}
