package com.rekchina.rocketmq.broker;

import com.rekchina.rocketmq.netty.BrokerRemotingServer;
import com.rekchina.rocketmq.netty.Message;
import com.rekchina.rocketmq.processor.PullMessageService;
import com.rekchina.rocketmq.processor.SendMessageService;
import com.rekchina.rocketmq.protocol.CommandType;
import com.rekchina.rocketmq.store.ConsumeQueue;
import com.rekchina.rocketmq.store.ConsumeQueueManage;
import com.rekchina.rocketmq.store.MessageFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Broker控制器
 * 1.启动服务端接收生产者发送消息请求
 * 2.将消息持久化到文件
 * 3.接收消费者拉取消息请求
 * Created by Administrator on 2016/9/1.
 */
public class BrokerController {

    // 消息存储文件
    private MessageFile messageFile;
    // 消费进度列表
    private ConsumeQueue consumeQueue = new ConsumeQueue();
    private ConsumeQueueManage consumeQueueManage;
    // 定时将消费进度刷盘到文件中
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public BrokerController() {
        try {
            this.consumeQueueManage = new ConsumeQueueManage(this);
            this.consumeQueueManage.load();

            scheduledExecutorService.scheduleAtFixedRate(new ConsumeQueueService(), 0, 5000, TimeUnit.MILLISECONDS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    /**
     * 新增消息
     * @param topic
     * @param message
     */
    public void addMessage(String topic, Message message) {
        // 保存到消息文件
        try {
            messageFile = new MessageFile(topic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        messageFile.appendMessage(message);
    }

    /**
     * 获取消息
     * @param topic 主题
     * @param offset 消费进度
     * @return
     */
    public Message getMessage(String topic, int offset) {
        // 从文件中读消息
        try {
            messageFile = new MessageFile(topic);
            Message message = messageFile.getMessage(offset);
            return message;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取消息
     * @param topic
     * @return
     */
    public Message getMessage(String topic) {
        return getMessage(topic, 0);
    }

    /**
     * 获取消费进度
     * @param consumer
     * @param topic
     * @return
     */
    public int getConsumeOffsetStore(String consumer, String topic) {
        return this.consumeQueue.getConsumeOffsetStore(topic, consumer);
    }

    /**
     * 更新消费进度
     * @param consumer
     * @param topic
     * @param offset
     */
    public void updateConsumeOffsetStore(String consumer, String topic, int offset) {
        offset++;
        this.consumeQueue.updateConsumeOffsetStore(topic, consumer, offset);
    }

    public ConsumeQueue getConsumeQueue() {
        return consumeQueue;
    }

    public void setConsumeQueue(ConsumeQueue consumeQueue) {
        this.consumeQueue = consumeQueue;
    }

    public ConsumeQueueManage getConsumeQueueManage() {
        return consumeQueueManage;
    }

    class ConsumeQueueService implements Runnable {

        @Override
        public void run() {
            BrokerController.this.getConsumeQueueManage().persist();
        }
    }
}
