package com.rekchina.netty.share.message;

import com.rekchina.netty.share.BaseMsg;
import com.rekchina.netty.share.MsgType;

/**
 * 响应类型消息
 * Created by Administrator on 2016/8/22.
 */
public class ReplyMsg extends BaseMsg {

    private ReplyBody replyBody;

    public ReplyMsg() {
        super();
        setMsgType(MsgType.REPLY);
    }

    public ReplyBody getReplyBody() {
        return replyBody;
    }

    public void setReplyBody(ReplyBody replyBody) {
        this.replyBody = replyBody;
    }
}
