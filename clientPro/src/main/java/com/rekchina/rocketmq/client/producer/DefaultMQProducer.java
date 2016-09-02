package com.rekchina.rocketmq.client.producer;

import com.rekchina.rocketmq.netty.Message;
import com.rekchina.rocketmq.netty.ProducerRemotingClient;
import com.rekchina.rocketmq.protocol.CommandType;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import com.rekchina.rocketmq.protocol.RemotingSerializable;

/**
 * 生产者
 * 1.启动客户端连接Broker，保持长连接
 * 2.发送消息
 * Created by Administrator on 2016/8/19.
 */
public class DefaultMQProducer {

    private String producerName;
    private ProducerRemotingClient producerRemotingClient;

    public DefaultMQProducer(String producerName) {
        this.producerName = producerName;
    }

    /**
     * Start a client to connect server
     */
    public void start() {
        this.producerRemotingClient = new ProducerRemotingClient();
        this.producerRemotingClient.start();
    }

    /**
     * Send message to the server
     * @param message
     */
    public void send(Message message) {

        RemotingProCommand request = new RemotingProCommand();
        request.setCommandType(CommandType.SEND_MESSAGE);
        request.setHeader(producerName.getBytes());
        request.setBody(RemotingSerializable.toJson(message).getBytes());

        this.producerRemotingClient.invoke(request);
    }

}
