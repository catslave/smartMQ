package com.rekchina.rocketmq.protocol;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 远程通信协议
 * Created by Administrator on 2016/9/1.
 */
public class RemotingProCommand implements Serializable {

    public static final long serialVersionUID = 1L;

    private CommandType commandType;

    private byte[] header;

    private byte[] body;

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    /**
     * encode RemotingProCommand to ByteBuffer
     * @return
     */
    public ByteBuffer encode() {
        int length = 4;
        length += this.header.length;
        length += this.body.length;

        ByteBuffer byteBuffer = ByteBuffer.allocate(length);

        byteBuffer.putInt(this.header.length);
        byteBuffer.put(this.getCommandType().toString().getBytes());
        byteBuffer.put(this.header);
        byteBuffer.put(this.body);

        byteBuffer.flip();
        return byteBuffer;
    }

    /**
     * decode ByteBuffer to RemotingProCommand
     * @param byteBuffer
     * @return
     */
    public RemotingProCommand decode(ByteBuffer byteBuffer) {
        int length = byteBuffer.limit();
        int headerLength = byteBuffer.getInt();

        byte[] headerData = new byte[headerLength];
        byteBuffer.get(headerData);

        int bodyLength = length - headerLength - 4;
        byte[] bodyData = new byte[bodyLength];
        byteBuffer.get(bodyData);

        RemotingProCommand remotingProCommand = new RemotingProCommand();
        remotingProCommand.setHeader(headerData);
        remotingProCommand.setBody(bodyData);

        return remotingProCommand;
    }
}
