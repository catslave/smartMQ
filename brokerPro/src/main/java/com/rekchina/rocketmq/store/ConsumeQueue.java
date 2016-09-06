package com.rekchina.rocketmq.store;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * 消费队列 保存每个消费者的消息进度
 * Created by Administrator on 2016/9/6.
 */
public class ConsumeQueue {

    private HashMap<String/* topic */, Integer/* offset */> offsetsTable = new HashMap<>();

    /**
     * 更新消费进度
     * @param topic
     * @param offset
     */
    public void updateConsumeOffsetStore(String topic, int offset) {
        offsetsTable.put(topic, offset);
    }

    /**
     * 获取消费进度
     * @param topic
     */
    public int getConsumeOffsetStore(String topic) {
        if(!offsetsTable.containsKey(topic)) {
            offsetsTable.put(topic, 0);
        }
        return offsetsTable.get(topic);
    }

}
