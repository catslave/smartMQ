package com.rekchina.rocketmq.store;

import com.rekchina.rocketmq.broker.BrokerController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * 持久化消费进度
 *  参考：ConsumerOffsetManager和ConfigManager
 * Created by Administrator on 2016/9/6.
 */
public class ConsumeQueueManage {

    private File file;
    private RandomAccessFile randomAccessFile;
    private final String fileName = "consumerOffset.json";
    private String storePath = "brokerPro/target/sotre/config/"; // 消息进度存储路径
    private BrokerController brokerController;

    public ConsumeQueueManage(BrokerController brokerController) throws FileNotFoundException {
        this.brokerController = brokerController;
        this.file = new File(this.storePath + this.fileName);
        this.randomAccessFile = new RandomAccessFile(this.file, "rw");
    }

    /**
     * 加载文件
     */
    public void load() {

    }

    /**
     * 持久化到文件
     */
    public void persist() {

    }

    class ConsumeQueueList {

    }
}
