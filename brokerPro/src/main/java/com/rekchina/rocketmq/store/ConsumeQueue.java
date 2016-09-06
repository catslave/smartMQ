package com.rekchina.rocketmq.store;

import com.rekchina.rocketmq.protocol.RemotingSerializable;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * 消费队列 保存每个消费者的消息进度
 * Created by Administrator on 2016/9/6.
 */
public class ConsumeQueue {

    private HashMap<String/* topic@consumer */, Integer/* offset */> offsetsTable = new HashMap<>();

    public ConsumeQueue() {

    }

    /**
     * 更新消费进度
     * @param topic
     * @param consumer
     * @param offset
     */
    public void updateConsumeOffsetStore(String topic, String consumer, int offset) {
        String key = topic + "@" + consumer;
        offsetsTable.put(key, offset);
    }

    /**
     * 获取消费进度
     * @param topic
     * @param consumer
     */
    public int getConsumeOffsetStore(String topic, String consumer) {
        String key = topic + "@" + consumer;
        if(!offsetsTable.containsKey(key)) {
            offsetsTable.put(key, 0);
        }
        return offsetsTable.get(key);
    }

    public HashMap<String, Integer> getOffsetsTable() {
        return offsetsTable;
    }

    public void setOffsetsTable(HashMap<String, Integer> offsetsTable) {
        this.offsetsTable = offsetsTable;
    }

    public String encode() {
        return RemotingSerializable.toJson(this);
    }
}
