package com.rekchina.rocketmq.netty;

import com.rekchina.rocketmq.protocol.RemotingProCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * 远程通信协议 编码器
 * Created by Administrator on 2016/9/1.
 */
public class RemotingEncoder extends MessageToByteEncoder<RemotingProCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RemotingProCommand msg, ByteBuf out) throws Exception {
        ByteBuffer byteBuffer = msg.encode();
        out.writeBytes(byteBuffer);
    }
}
