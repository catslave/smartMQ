package com.rekchina.rocketmq.store;

import com.rekchina.rocketmq.netty.Message;
import com.rekchina.rocketmq.protocol.RemotingSerializable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 消息存储文件
 * Created by Administrator on 2016/9/6.
 */
public class MessageFile {

    private File file;
    private RandomAccessFile randomAccessFile;
    private final int MESSAGE_SIZE = 128; // 消息大小
    private String storePath = "brokerPro/target/store/commitlog/";// 消息存储路径

    public MessageFile(String fileName) throws FileNotFoundException {
        this.file = new File(this.storePath + fileName);
        this.randomAccessFile = new RandomAccessFile(this.file, "rw");
    }

    /**
     * 添加消息
     * @param message
     * @return
     */
    public void appendMessage(Message message) {
        try {
            byte[] byteBuffer = new byte[MESSAGE_SIZE - 4];// 1M字节的存储空间
            byte[] messageBytes = RemotingSerializable.toJson(message).getBytes();
            System.arraycopy(messageBytes, 0, byteBuffer, 0, messageBytes.length);

            this.randomAccessFile.seek(this.randomAccessFile.length());// 追加到文件末尾
            this.randomAccessFile.writeInt(messageBytes.length);// 4个字节
            this.randomAccessFile.write(byteBuffer);
            this.randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取消息
     * @param offset
     * @return
     */
    public Message getMessage(int offset) {
        try {
            this.randomAccessFile.seek(offset * MESSAGE_SIZE);
            int msgLength = this.randomAccessFile.readInt();// 获取消息长度

            byte[] msgBodyByte = new byte[msgLength];
            this.randomAccessFile.read(msgBodyByte, 0, msgLength);

            Message message = RemotingSerializable.fromJson(new String(msgBodyByte), Message.class);// 反序列化消息
            return message;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
