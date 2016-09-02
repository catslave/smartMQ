package com.rekchina.rocketmq.netty;

import com.rekchina.rocketmq.protocol.CommandType;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import io.netty.bootstrap.Bootstrap;
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
    //Request processor. When receive the broker response, than invoke requestProcessor.
    private RemotingProcessor remotingProcessor;

    public ProducerRemotingClient() {
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup();
    }

    public ProducerRemotingClient(RemotingProcessor remotingProcessor) {
        this();
        this.remotingProcessor = remotingProcessor;
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
//                        socketChannel.pipeline().addLast(new RemotingEncoder());
//                        socketChannel.pipeline().addLast(new RemotingDecoder());
                        socketChannel.pipeline().addLast(new ObjectEncoder());
                        socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                        socketChannel.pipeline().addLast(new ProducerClientHandler());
                    }
                });
    }

    /**
     * send request to broker
     *
     * @param remotingProCommand
     */
    public void invoke(RemotingProCommand remotingProCommand) {
        if (this.channelFuture == null || !this.channelFuture.isSuccess() || !this.channelFuture.channel().isActive()) {
            connect();
        }

        if(!this.channelFuture.isSuccess()) {
            System.out.println("connect the broker failed");
            return;
        }

        this.channelFuture.channel().writeAndFlush(remotingProCommand).addListener(new ChannelFutureListener() {
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
            Thread.sleep(1000);
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
            if(CommandType.PULL_MESSAGE == msg.getCommandType()) {
                if(remotingProcessor != null) {
                    remotingProcessor.processRequest(ctx, msg);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            // Close the connection when an exception is raised.
//            cause.printStackTrace();
            System.out.println("the server connection is closed");
            ctx.close();
        }
    }
}
