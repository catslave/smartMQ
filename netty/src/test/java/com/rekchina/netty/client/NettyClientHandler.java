package com.rekchina.netty.client;

import com.rekchina.netty.share.BaseMsg;
import com.rekchina.netty.share.MsgType;
import com.rekchina.netty.share.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Administrator on 2016/8/22.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<BaseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {

        MsgType msgType = baseMsg.getMsgType();
        switch (msgType) {
            case LOGIN: {
                LoginMsg loginMsg = new LoginMsg();
                loginMsg.setUserName("yangjj");
                loginMsg.setPassword("yangjj");
                channelHandlerContext.writeAndFlush(loginMsg);
            }
            break;
            case PING: {
                System.out.println("receive ping from server ......");
            }
            break;
            case ASK: {
                ReplyClientBody replyClientBody = new ReplyClientBody("client info ......");
                ReplyMsg replyMsg = new ReplyMsg();
                replyMsg.setReplyBody(replyClientBody);
                channelHandlerContext.writeAndFlush(replyMsg);
            }
            break;
            case REPLY: {
                ReplyMsg replyMsg = (ReplyMsg) baseMsg;
                ReplyServerBody replyServerBody = (ReplyServerBody) replyMsg.getReplyBody();
                System.out.println("receive server msg: " + replyServerBody.getServerInfo());
            }
            break;
            default:
                break;
        }
        ReferenceCountUtil.release(msgType);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case WRITER_IDLE:
                    PingMsg pingMsg = new PingMsg();
                    ctx.writeAndFlush(pingMsg);
                    System.out.println("send ping to server ......");
                    break;
                default:
                    break;
            }
        }
    }
}

