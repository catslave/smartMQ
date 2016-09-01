package com.rekchina.netty.share.message;

import com.rekchina.netty.share.BaseMsg;
import com.rekchina.netty.share.MsgType;

/**
 * 心跳检查Ping类型消息
 * Created by Administrator on 2016/8/22.
 */
public class PingMsg extends BaseMsg {

    public PingMsg() {
        super();
        setMsgType(MsgType.PING);
    }
}
