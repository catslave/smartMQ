package com.rekchina.rocketmq.netty;

import com.alibaba.rocketmq.remoting.protocol.RemotingCommand;
import com.rekchina.rocketmq.netty.RemotingDecoder;
import com.rekchina.rocketmq.netty.RemotingEncoder;
import com.rekchina.rocketmq.protocol.CommandType;
import com.rekchina.rocketmq.protocol.RemotingProCommand;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.marshalling.CompatibleMarshallingDecoder;
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
    // Processors
    private HashMap<CommandType, RemotingProcessor> processorTable = new HashMap<>();

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
//                        socketChannel.pipeline().addLast(new RemotingEncoder());
//                        socketChannel.pipeline().addLast(new RemotingDecoder());
                        socketChannel.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        socketChannel.pipeline().addLast(new ObjectEncoder());
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
     * register processor
     * @param commandType
     * @param remotingProcessor
     */
    public void register(CommandType commandType, RemotingProcessor remotingProcessor) {
        this.processorTable.put(commandType, remotingProcessor);
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
            RemotingProcessor remotingProcessor = processorTable.get(msg.getCommandType());
            remotingProcessor.processRequest(ctx, msg);


        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            // Close the connection when an exception is raised.
//            cause.printStackTrace();
            System.out.println("The client collection is closed");
            ctx.close();
        }
    }
}
