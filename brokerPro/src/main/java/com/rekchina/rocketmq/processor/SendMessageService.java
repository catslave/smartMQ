package com.rekchina.rocketmq.processor;

import com.rekchina.rocketmq.broker.BrokerController;
import com.rekchina.rocketmq.netty.Message;
import com.rekchina.rocketmq.netty.RemotingProcessor;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import com.rekchina.rocketmq.protocol.RemotingSerializable;
import io.netty.channel.ChannelHandlerContext;

/**
 * Process the message
 * Created by Administrator on 2016/9/2.
 */
public class SendMessageService implements RemotingProcessor {

    private BrokerController brokerController;

    public SendMessageService(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public void processRequest(ChannelHandlerContext ctx, RemotingProCommand remotingProCommand) {
        Message message = RemotingSerializable.fromJson(new String(remotingProCommand.getBody()), Message.class);
        brokerController.addMessage(message.getTopic(), message);
        System.out.println("append message:");
        System.out.println(new String(remotingProCommand.getHeader()) + ":" + new String(remotingProCommand.getBody()));
    }
}
