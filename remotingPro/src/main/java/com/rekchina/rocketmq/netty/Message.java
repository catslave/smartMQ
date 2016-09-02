package com.rekchina.rocketmq.netty;

import java.io.Serializable;

/**
 * Message
 * Created by Administrator on 2016/9/2.
 */
public class Message implements Serializable {

    public static final long serialVersionUID = 1L;

    private String topic;

    private byte[] body;

    public Message() {

    }

    public Message(String topic, byte[] body) {
        this.topic = topic;
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
