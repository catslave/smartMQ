package com.rekchina.netty.server;

import com.rekchina.netty.share.message.AskMsg;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/22.
 */
public class NettyServerBootstrap {
    
    private int port;
    private SocketChannel socketChannel;
    
    public NettyServerBootstrap(int port) throws InterruptedException {
        this.port = port;
        bind();
    }

    private void bind() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new NettyServerHandler()
                                );
                    }
                });

        ChannelFuture future = serverBootstrap.bind(port).sync();
        if(future.isSuccess()) {
            System.out.println("server start ......");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServerBootstrap(9080);
//        while(true) {
//            Channel channel = NettyChannelMap.get("001");
//            if(channel != null) {
//                AskMsg askMsg = new AskMsg();
//                channel.writeAndFlush(askMsg);
//            }
//            TimeUnit.SECONDS.sleep(5);
//        }
    }
}
