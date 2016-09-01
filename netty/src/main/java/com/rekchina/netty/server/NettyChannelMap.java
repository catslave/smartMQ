package com.rekchina.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map 对SocketChannel引用的Map
 * Created by Administrator on 2016/8/22.
 */
public class NettyChannelMap {

    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();

    public static void add(String clientId, SocketChannel socketChannel) {
        map.put(clientId, socketChannel);
    }

    public static Channel get(String clientId) {
        return map.get(clientId);
    }

    public static void remove(SocketChannel socketChannel) {
        for(Map.Entry entry : map.entrySet()) {
            if(entry.getValue() == socketChannel) {
                map.remove(entry.getKey());
            }
        }
    }
}
