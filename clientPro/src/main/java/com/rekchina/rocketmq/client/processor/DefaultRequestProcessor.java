package com.rekchina.rocketmq.client.processor;

import com.rekchina.rocketmq.netty.Message;
import com.rekchina.rocketmq.netty.RemotingProcessor;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import com.rekchina.rocketmq.protocol.RemotingSerializable;
import io.netty.channel.ChannelHandlerContext;

/**
 * the default request processor
 * Created by Administrator on 2016/9/2.
 */
public class DefaultRequestProcessor implements RemotingProcessor {

    public void processRequest(ChannelHandlerContext ctx, RemotingProCommand remotingProCommand) {
        try {
            Message message = RemotingSerializable.fromJson(new String(remotingProCommand.getBody()), Message.class);
            System.out.println("consume :" + message.getTopic() + "," + new String(message.getBody()));
        } catch (Exception ex) {
            System.out.println("consume failed");
        }

    }
}
