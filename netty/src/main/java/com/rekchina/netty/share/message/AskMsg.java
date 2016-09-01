package com.rekchina.netty.share.message;

import com.rekchina.netty.share.BaseMsg;
import com.rekchina.netty.share.MsgType;

/**
 * 请求类型消息
 * Created by Administrator on 2016/8/22.
 */
public class AskMsg extends BaseMsg{

    private AskParams askParams;

    public AskMsg() {
        super();
        setMsgType(MsgType.ASK);
    }

    public AskParams getAskParams() {
        return askParams;
    }

    public void setAskParams(AskParams askParams) {
        this.askParams = askParams;
    }
}
