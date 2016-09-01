package com.rekchina.netty.server;

import com.rekchina.netty.share.BaseMsg;
import com.rekchina.netty.share.MsgType;
import com.rekchina.netty.share.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Administrator on 2016/8/22.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {

        if (MsgType.LOGIN.equals(baseMsg.getMsgType())) {
            LoginMsg loginMsg = (LoginMsg) baseMsg;
            if ("yangjj".equals(loginMsg.getUserName()) && "yangjj".equals(loginMsg.getPassword())) {
                NettyChannelMap.add(loginMsg.getClientId(), (SocketChannel) channelHandlerContext.channel());
                System.out.println("client " + loginMsg.getClientId() + " login success");
            }
        } else {
            if (NettyChannelMap.get(baseMsg.getClientId()) == null) {
                LoginMsg loginMsg = new LoginMsg();
                channelHandlerContext.channel().writeAndFlush(loginMsg);
            }
        }

        switch (baseMsg.getMsgType()) {
            case PING: {
                PingMsg pingMsg = (PingMsg) baseMsg;
                PingMsg replyPing = new PingMsg();
                NettyChannelMap.get(pingMsg.getClientId()).writeAndFlush(replyPing);
            }
            break;
            case ASK: {
                AskMsg askMsg = (AskMsg) baseMsg;
                if("authToken".equals(askMsg.getAskParams().getAuth())) {
                    ReplyServerBody replyServerBody = new ReplyServerBody("server info......");
                    ReplyMsg replyMsg = new ReplyMsg();
                    replyMsg.setReplyBody(replyServerBody);
                    NettyChannelMap.get(askMsg.getClientId()).writeAndFlush(replyMsg);
                }
            }
            break;
            case REPLY: {
                ReplyMsg replyMsg = (ReplyMsg) baseMsg;
                ReplyClientBody replyClientBody = (ReplyClientBody) replyMsg.getReplyBody();
                System.out.println("receive client msg: " + replyClientBody.getClientInfo());
            }
            break;
            default:
                break;
        }
        ReferenceCountUtil.release(baseMsg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelMap.remove((SocketChannel) ctx.channel());
        System.out.println("remove client");
    }
}

