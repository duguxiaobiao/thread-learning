package com.lonely.设计模式在多线程中的体现.GuardedSuspension_暂停保护模式;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/23 17:05
 * Description: 客户端，一直发请求
 */
public class RequestClient extends Thread {

    private LinkedList<Request> requests;

    public RequestClient(LinkedList<Request> requests) {
        this.requests = requests;
    }

    @Override
    public void run() {

        //生成100个任务
        for (int i = 0; i < 100; i++) {
            synchronized (requests) {
                System.out.println(MessageFormat.format("{0}推送数据：{1}", Thread.currentThread().getName(), i));
                requests.addLast(new Request(MessageFormat.format("{0}-----{1}", Thread.currentThread().getName(), i)));
                requests.notifyAll();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
