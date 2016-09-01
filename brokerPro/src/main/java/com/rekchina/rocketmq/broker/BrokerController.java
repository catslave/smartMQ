package com.rekchina.rocketmq.broker;

/**
 * Broker控制器
 * 1.启动服务端接收生产者发送消息请求
 * 2.将消息持久化到文件
 * 3.接收消费者拉取消息请求
 * Created by Administrator on 2016/9/1.
 */
public class BrokerController {

    /**
     * Start the Broker Server
     * @param args
     */
    public static void main(String[] args) {
        new BrokerRemotingServer().start();
    }
}
