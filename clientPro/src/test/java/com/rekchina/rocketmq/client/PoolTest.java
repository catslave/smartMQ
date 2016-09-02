package com.rekchina.rocketmq.client;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/9/2.
 */
public class PoolTest {

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Test
    public void execute() {
        scheduledExecutorService.scheduleAtFixedRate(new MyThread(), 0, 2000, TimeUnit.MILLISECONDS);
    }
}

class MyThread implements Runnable {

    @Override
    public void run() {
        System.out.println("...");
    }
}
