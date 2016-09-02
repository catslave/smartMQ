package com.rekchina.rocketmq.processor;

import com.rekchina.rocketmq.broker.BrokerController;
import com.rekchina.rocketmq.netty.Message;
import com.rekchina.rocketmq.netty.RemotingProcessor;
import com.rekchina.rocketmq.protocol.CommandType;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import com.rekchina.rocketmq.protocol.RemotingSerializable;
import io.netty.channel.ChannelHandlerContext;

/**
 * Process pull message
 * Created by Administrator on 2016/9/2.
 */
public class PullMessageService implements RemotingProcessor {

    private BrokerController brokerController;

    public PullMessageService(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public void processRequest(ChannelHandlerContext ctx, RemotingProCommand remotingProCommand) {
//        System.out.println("PULL_MESSAGE");
        //拉取消息
        Message message = brokerController.getMessage();
        if(message == null) {
            return;
        }
        RemotingProCommand responseCommand = new RemotingProCommand();
        responseCommand.setCommandType(CommandType.PULL_MESSAGE);
        responseCommand.setBody(RemotingSerializable.toJson(message).getBytes());
        ctx.channel().write(responseCommand);
        ctx.channel().flush();
        System.out.println("consume message: " + message);
    }
}
