package com.rekchina.rocketmq.store;

import com.rekchina.rocketmq.broker.BrokerController;
import com.rekchina.rocketmq.protocol.RemotingSerializable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private String storePath = "brokerPro/target/store/config/"; // 消息进度存储路径
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

        File file = new File(this.storePath + this.fileName);
        if(file.exists()) {
            char[] data = new char[(int) file.length()];
            boolean result = false;

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                int len = fileReader.read(data);
                result = (len == data.length);
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
            finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            if (result) {
                String value = new String(data);
                try {
                    ConsumeQueue consumeQueue = RemotingSerializable.fromJson(value, ConsumeQueue.class);
                    this.brokerController.setConsumeQueue(consumeQueue);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    /**
     * 持久化到文件
     */
    public void persist() {

        String data = this.brokerController.getConsumeQueue().encode();

        File file = new File(this.storePath + this.fileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

}
