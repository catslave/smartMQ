package com.rekchina.netty.share.message;

import com.rekchina.netty.share.BaseMsg;
import com.rekchina.netty.share.MsgType;

/**
 * 登录类型消息
 * Created by Administrator on 2016/8/22.
 */
public class LoginMsg extends BaseMsg {

    private String userName;
    private String password;

    public LoginMsg() {
        super();
        setMsgType(MsgType.LOGIN);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
