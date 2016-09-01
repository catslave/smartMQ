package com.rekchina.rocketmq.broker;

import com.rekchina.rocketmq.netty.RemotingDecoder;
import com.rekchina.rocketmq.netty.RemotingEncoder;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Broker 服务端
 * Created by Administrator on 2016/9/1.
 */
public class BrokerRemotingServer {

    //服务端
    private ServerBootstrap serverBootstrap;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    //监听端口
    private int port = 10911;
    //消息队列
    private List<String> messages = new ArrayList<>();

    public BrokerRemotingServer() {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RemotingEncoder());
                        socketChannel.pipeline().addLast(new RemotingDecoder());
                        socketChannel.pipeline().addLast(new BrokerServerHandler());
                    }
                });
    }

    /**
     * Start the server
     */
    public void start() {
        try {
            // Bind and start to accept incoming connections.
            ChannelFuture channelFuture = this.serverBootstrap.bind(this.port).sync();

            if(channelFuture.isSuccess()) {
                System.out.println("the server start success!");
            }

            // Wait until the server socket is closed.
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.workerGroup.shutdownGracefully();
            this.bossGroup.shutdownGracefully();
        }
    }

    /**
     * Handles a server-side channel.
     */
    class BrokerServerHandler extends SimpleChannelInboundHandler<RemotingProCommand> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("a client connected");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingProCommand msg) throws Exception {
            System.out.println("receive the message");

            if(msg.getHeader().length == 0) {
                //拉取消息
                for(String message : messages) {
                    RemotingProCommand remotingProCommand = new RemotingProCommand();
                    remotingProCommand.setBody(message.getBytes());
                    ctx.write(remotingProCommand);
                    System.out.println("consume message: " + message);
                }
                ctx.flush();
                messages.clear();
            } else {
                //发送消息
                messages.add(new String(msg.getBody()));
                System.out.println(new String(msg.getHeader()) + ":" + new String(msg.getBody()));
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }
    }
}
