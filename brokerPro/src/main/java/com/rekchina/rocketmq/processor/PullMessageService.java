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
        // 拉取消息
        String header = new String(remotingProCommand.getHeader());
        String topic = header.split("@")[0];
        String consumerName = header.split("@")[1];

        // 获取消费进度
        int offsetStore = this.brokerController.getConsumeOffsetStore(consumerName, topic);

        // 获取消息
        Message message = brokerController.getMessage(topic, offsetStore);
        if(message == null) {
            return;
        }

        // 更新消费进度
        this.brokerController.updateConsumeOffsetStore(consumerName, topic, offsetStore);

        // 返回消息
        RemotingProCommand responseCommand = new RemotingProCommand();
        responseCommand.setCommandType(CommandType.PULL_MESSAGE);
        responseCommand.setBody(RemotingSerializable.toJson(message).getBytes());
        ctx.channel().write(responseCommand);
        ctx.channel().flush();
        System.out.println("consume message: " + message);
    }
}
