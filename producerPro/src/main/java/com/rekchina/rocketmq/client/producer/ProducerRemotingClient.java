package com.rekchina.rocketmq.client.producer;

import com.rekchina.rocketmq.netty.RemotingDecoder;
import com.rekchina.rocketmq.netty.RemotingEncoder;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Producer客户端
 * Created by Administrator on 2016/9/1.
 */
public class ProducerRemotingClient {

    //服务端
    private Bootstrap bootstrap;
    private NioEventLoopGroup workerGroup;
    //连接端口
    private int port = 10911;
    //连接地址
    private String host = "127.0.0.1";
    private ChannelFuture channelFuture;

    public ProducerRemotingClient() {
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup();
    }

    /**
     * Start the client
     */
    public void start() {

        this.bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RemotingEncoder());
                        socketChannel.pipeline().addLast(new RemotingDecoder());
                        socketChannel.pipeline().addLast(new ProducerClientHandler());
                    }
                });
    }

    /**
     * send message to broker
     *
     * @param message
     */
    public void invoke(String producerName, String message) {
        if (this.channelFuture == null) {
            connect();
        }

        RemotingProCommand request = new RemotingProCommand();
        request.setHeader(producerName.getBytes());
        request.setBody(message.getBytes());

        this.channelFuture.channel().writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("send success");
                } else {
                    System.out.println("send failed");
                }
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        // connect to the server
        this.channelFuture = this.bootstrap.connect(this.host, this.port);

        if (this.channelFuture.awaitUninterruptibly(3000)) {
            if (channelFuture.isSuccess()) {
                System.out.println("connect to the broker success!");
            } else {
                System.out.println("connect to the broker timeout!");
            }
        }
    }

    /**
     * Handles a client-side channel.
     */
    class ProducerClientHandler extends SimpleChannelInboundHandler<RemotingProCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingProCommand msg) throws Exception {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }
    }
}
