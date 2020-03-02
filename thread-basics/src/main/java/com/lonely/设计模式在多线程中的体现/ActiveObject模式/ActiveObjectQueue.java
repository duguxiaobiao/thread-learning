package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

import java.util.LinkedList;

/**
 * @auther: 15072
 * @date: 2020/2/25 20:07
 * Description: action队列
 */
public class ActiveObjectQueue {

    /**
     * 队列最大容量
     */
    private final static int MAX_QUEUE_SIZE = 100;

    private final LinkedList<MethodRequest> requestsQueue;


    public ActiveObjectQueue() {
        this.requestsQueue = new LinkedList<>();
    }

    /**
     * 放入任务到队列中
     *
     * @param methodRequest
     */
    public void put(MethodRequest methodRequest) {
        synchronized (this.requestsQueue) {
            while (this.requestsQueue.size() >= MAX_QUEUE_SIZE) {
                try {
                    this.requestsQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.requestsQueue.addLast(methodRequest);
            this.requestsQueue.notifyAll();
        }
    }

    /**
     * 从队列中取出任务
     *
     * @return
     */
    public MethodRequest take() {
        synchronized (this.requestsQueue) {
            while (this.requestsQueue.isEmpty()) {
                try {
                    this.requestsQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            MethodRequest methodRequest = this.requestsQueue.removeFirst();
            this.requestsQueue.notifyAll();
            return methodRequest;
        }
    }

}
