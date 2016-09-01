package com.rekchina.netty.client;

import com.rekchina.netty.share.Constants;
import com.rekchina.netty.share.message.LoginMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by Administrator on 2016/8/22.
 */
public class NettyClientBootstrap {

    private int port;
    private String host;
    private SocketChannel socketChannel;
    public static final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(20);

    public NettyClientBootstrap(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
        start();
    }

    private void start() throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .group(eventLoopGroup)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new IdleStateHandler(20, 10, 0),
                                new NettyClientHandler()
                        );
                    }
                });

        ChannelFuture future = bootstrap.connect(host, port).sync();
        if(future.isSuccess()) {
            socketChannel = (SocketChannel) future.channel();
            System.out.println("connect server success ......");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Constants.setClientId("001");
        NettyClientBootstrap clientBootstrap = new NettyClientBootstrap(9080, "172.19.0.40");

        LoginMsg loginMsg = new LoginMsg();
        loginMsg.setUserName("yangjj");
        loginMsg.setPassword("yangjj");
        clientBootstrap.socketChannel.writeAndFlush(loginMsg);
//        while(true) {
//            TimeUnit.SECONDS.sleep(3);
//            AskMsg askMsg = new AskMsg();
//            AskParams askParams = new AskParams();
//            askParams.setAuth("authToken");
//            askMsg.setAskParams(askParams);
//            clientBootstrap.socketChannel.writeAndFlush(askMsg);
//        }
    }
}
