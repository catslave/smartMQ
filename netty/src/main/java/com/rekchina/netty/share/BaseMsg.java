package com.rekchina.netty.share;

import java.io.Serializable;

/**
 * Message基类
 * Created by Administrator on 2016/8/22.
 */
public abstract class BaseMsg implements Serializable {

    public static final long serialVersionUID = 1L;
    private MsgType msgType;
    private String clientId;

    public BaseMsg() {
        this.clientId = Constants.getClientId();
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
