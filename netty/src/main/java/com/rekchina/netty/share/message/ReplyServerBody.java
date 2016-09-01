package com.rekchina.netty.share.message;

/**
 * 服务端响应Body对象
 * Created by Administrator on 2016/8/22.
 */
public class ReplyServerBody extends ReplyBody {

    private String serverInfo;

    public ReplyServerBody(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }
}
