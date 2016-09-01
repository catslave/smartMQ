package com.rekchina.rocketmq.netty;

import com.rekchina.rocketmq.protocol.RemotingProCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 远程通信协议 解码器
 * Created by Administrator on 2016/9/1.
 */
public class RemotingDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        RemotingProCommand remotingProCommand = new RemotingProCommand().decode(in.nioBuffer());
        out.add(remotingProCommand);
    }
}
