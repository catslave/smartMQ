package com.rekchina.rocketmq.client.consumer;

import com.rekchina.rocketmq.protocol.CommandType;
import com.rekchina.rocketmq.protocol.RemotingProCommand;

/**
 * Long polling messages
 * Created by Administrator on 2016/9/2.
 */
public class PullMessageService implements Runnable {

    private DefaultMQConsumer defaultMQConsumer;

    public PullMessageService(DefaultMQConsumer defaultMQConsumer) {
        this.defaultMQConsumer = defaultMQConsumer;
    }

    @Override
    public void run() {
        System.out.println("execute pull message service");
        RemotingProCommand request = new RemotingProCommand();
        request.setCommandType(CommandType.PULL_MESSAGE);
        request.setHeader(this.defaultMQConsumer.getConsumerName().getBytes());

        this.defaultMQConsumer.getProducerRemotingClient().invoke(request);
    }
}
