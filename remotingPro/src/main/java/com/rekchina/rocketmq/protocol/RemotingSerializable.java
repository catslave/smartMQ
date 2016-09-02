package com.rekchina.rocketmq.protocol;

import com.alibaba.fastjson.JSON;

/**
 * Object serialize helper
 * Created by Administrator on 2016/9/2.
 */
public class RemotingSerializable {

    public static String toJson(final Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }

}
