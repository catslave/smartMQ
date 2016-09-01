package com.rekchina.rocketmq.producer;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * Created by Administrator on 2016/8/16.
 */
public class Producer {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("Producer");
//        producer.setNamesrvAddr("172.19.0.89:9876");
        producer.setNamesrvAddr("112.124.50.158:9876");
//        producer.setInstanceName("t1");

        //同一个客户端实例，Group名称不能相同
        //不同的客户端实例，Group名称可以相同
        DefaultMQProducer producer1 = new DefaultMQProducer("Producer1");
        producer1.setNamesrvAddr("112.124.50.158:9876");
        producer1.setInstanceName("t1");

        DefaultMQProducer producer2 = new DefaultMQProducer("Producer");
        producer2.setNamesrvAddr("112.124.50.158:9876");
        producer2.setInstanceName("t3");
        try {
            producer.start();
//            producer1.start();
//            producer2.start();
//
            //发送第一条测试消息
            Message message = new Message("PushTopic-5", "push", "1", "Just for test.".getBytes());
            SendResult result = producer.send(message);
            System.out.println("id:" + result.getMsgId() + ", result:" + result.getSendStatus());
//
//            //发送第二条测试消息
//            message = new Message("PushTopic", "push", "2", "Just for test.".getBytes());
//            result = producer.send(message);
//            System.out.println("id:" + result.getMsgId() + ", result:" + result.getSendStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (RemotingException e) {
//            e.printStackTrace();
//        } catch (MQBrokerException e) {
//            e.printStackTrace();
//        }
    }
}
