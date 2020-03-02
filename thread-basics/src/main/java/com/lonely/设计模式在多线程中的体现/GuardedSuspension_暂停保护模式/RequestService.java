package com.lonely.设计模式在多线程中的体现.GuardedSuspension_暂停保护模式;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/23 16:59
 * Description: 消息处理，服务端
 */
public class RequestService extends Thread {


    private LinkedList<Request> requests;

    public RequestService(LinkedList<Request> requests) {
        this.requests = requests;
    }


    @Override
    public void run() {
        while (true) {
            synchronized (requests) {
                while (requests.isEmpty()) {
                    try {
                        requests.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Request request = requests.removeLast();
                System.out.println(MessageFormat.format("{0}执行：{1}", Thread.currentThread().getName(),
                        request.getName()));
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                requests.notifyAll();
            }


        }
    }
}
