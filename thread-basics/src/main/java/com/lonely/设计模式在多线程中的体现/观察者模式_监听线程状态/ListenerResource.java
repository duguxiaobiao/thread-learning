package com.lonely.设计模式在多线程中的体现.观察者模式_监听线程状态;

import java.io.Serializable;

/**
 * @auther: 15072
 * @date: 2020/2/20 17:43
 * Description: 监听资源，即用来传输消息的数据实体
 */
public class ListenerResource implements Serializable {

    /**
     * 监听中的线程对象
     */
    private Thread thread;

    /**
     * 监听中的线程对象的状态
     */
    private ThreadState threadState;

    /**
     * 线程对象异常对象
     */
    private Throwable throwable;

    public ListenerResource(Thread thread, ThreadState threadState, Throwable throwable) {
        this.thread = thread;
        this.threadState = threadState;
        this.throwable = throwable;
    }

    public Thread getThread() {
        return thread;
    }

    public ThreadState getThreadState() {
        return threadState;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
