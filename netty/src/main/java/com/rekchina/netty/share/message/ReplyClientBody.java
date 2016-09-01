package com.rekchina.netty.share.message;

/**
 * 客户端响应Body对象
 * Created by Administrator on 2016/8/22.
 */
public class ReplyClientBody extends ReplyBody {

    private String clientInfo;

    public ReplyClientBody(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }
}
