package com.rekchina.rocketmq.client.consumer;

import com.rekchina.rocketmq.client.processor.DefaultRequestProcessor;
import com.rekchina.rocketmq.netty.ProducerRemotingClient;
import com.rekchina.rocketmq.protocol.CommandType;
import com.rekchina.rocketmq.protocol.RemotingProCommand;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 消费者
 * Created by Administrator on 2016/9/1.
 */
public class DefaultMQConsumer {

    private String consumerName;
    private String subTopics;
    private ProducerRemotingClient producerRemotingClient;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();



    public DefaultMQConsumer(String consumerName) {
        this.consumerName = consumerName;
    }

    /**
     * start the client
     */
    public void start() {
        this.producerRemotingClient = new ProducerRemotingClient(new DefaultRequestProcessor());
        this.producerRemotingClient.start();
    }

    /**
     * subscribe topics
     * @param topic
     */
    public void subscribe(String topic) {
        this.subTopics = topic;
    }

    /**
     * pull messages
     * this is need long polling. How to implement?
     * 1.start a schedule fixed thread-pool execute the task of long-poll.
     * 2.commit the task to the thread-pool.
     * 3.register the processor to the thread-pool to deal with the response.
     */
    public void pull() {
        scheduledExecutorService.scheduleAtFixedRate(new PullMessageService(this), 0, 5000, TimeUnit.MILLISECONDS);
    }

    public String getConsumerName() {
        return consumerName;
    }

    public ProducerRemotingClient getProducerRemotingClient() {
        return producerRemotingClient;
    }

    public String getSubTopics() {
        return subTopics;
    }
}
