package com.rekchina.rocketmq.netty;

import com.rekchina.rocketmq.protocol.RemotingProCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * Process request.
 * Created by Administrator on 2016/9/2.
 */
public interface RemotingProcessor {

    void processRequest(ChannelHandlerContext ctx, RemotingProCommand remotingProCommand);
}
