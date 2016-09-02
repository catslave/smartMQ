package com.rekchina.rocketmq.broker;

import com.rekchina.rocketmq.netty.BrokerRemotingServer;
import com.rekchina.rocketmq.netty.Message;
import com.rekchina.rocketmq.processor.PullMessageService;
import com.rekchina.rocketmq.processor.SendMessageService;
import com.rekchina.rocketmq.protocol.CommandType;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Broker控制器
 * 1.启动服务端接收生产者发送消息请求
 * 2.将消息持久化到文件
 * 3.接收消费者拉取消息请求
 * Created by Administrator on 2016/9/1.
 */
public class BrokerController {

    //消息队列
    private HashMap<String/* topic */, BlockingQueue<Message>/* messageQueue */> messageBlockingQueue = new HashMap<>();

    /**
     * Start the Broker Server
     * @param args
     */
    public static void main(String[] args) {

        BrokerController brokerController = new BrokerController();

        BrokerRemotingServer brokerRemotingServer = new BrokerRemotingServer();
        brokerRemotingServer.register(CommandType.SEND_MESSAGE, new SendMessageService(brokerController));
        brokerRemotingServer.register(CommandType.PULL_MESSAGE, new PullMessageService(brokerController));
        brokerRemotingServer.start();
    }

    public void addMessage(String topic, Message message) {
        try {
            BlockingQueue<Message> messages = messageBlockingQueue.get(topic);
            if(messages == null) {
                messages = new LinkedBlockingDeque<>();
            }
            messages.put(message);
            messageBlockingQueue.put(topic, messages);
        } catch (InterruptedException e) {
            System.out.println("add message failed");
            e.printStackTrace();
        }
    }

    public Message getMessage(String topic) {
        BlockingQueue<Message> messages = messageBlockingQueue.get(topic);
        if(messages == null) {
            return null;
        }
        return messages.poll();
    }

}
