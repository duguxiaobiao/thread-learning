package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/25 20:14
 * Description: 任务调度线程
 */
public class SchedulerThread extends Thread {

    private final ActiveObjectQueue actionObjectQueue;


    public SchedulerThread(ActiveObjectQueue actionObjectQueue) {
        this.actionObjectQueue = actionObjectQueue;
    }

    public void invoke(MethodRequest methodRequest) {
        this.actionObjectQueue.put(methodRequest);
    }

    @Override
    public void run() {
        while (true) {
            MethodRequest methodRequest = this.actionObjectQueue.take();
            methodRequest.execute();
            //短暂休眠，模拟任务调度时长
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
