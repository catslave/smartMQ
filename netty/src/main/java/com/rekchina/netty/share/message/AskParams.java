package com.rekchina.netty.share.message;

import java.io.Serializable;

/**
 * 请求类型参数
 * Created by Administrator on 2016/8/22.
 */
public class AskParams implements Serializable {

    public static final long serialVersionUID = 1L;
    private String auth;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
