package com.lonely.多线程基础.线程池.手写线程池实现;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther: 15072
 * @date: 2020/2/17 12:30
 * Description: 自定义线程池---版本1：最简单实现
 */
public class CustomerThreadPool {

    /**
     * 默认线程池大小常量
     */
    private static final int DEFAULT_SIZE = 10;

    /**
     * 实际线程池大小
     */
    private volatile int size;

    private ThreadGroup threadGroup = new ThreadGroup("CUSTOMER_THREAD_POOL_GROUP");

    /**
     * 生成线程名的序列
     */
    private static final AtomicInteger seq = new AtomicInteger(0);

    /**
     * 工作线程命名规则前缀
     */
    public static final String THREAD_POOL_WORK_THREAD_NAME_PREFIX = "CUSTOMER_POOL_WORKTHREAD_";

    /**
     * 程序提交的Task
     */
    private LinkedList<Runnable> taskThreads = new LinkedList<>();

    /**
     * 自定义线程池的工作线程集合
     */
    private List<WorkThread> workThreads = new ArrayList<>();

    public CustomerThreadPool() {
        this(DEFAULT_SIZE);
    }

    public CustomerThreadPool(int size) {
        this.size = size;
        //创建线程池工作线程
        this.init();
    }

    /**
     * 提交任务
     *
     * @param task
     */
    public void submit(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task is null");
        }
        synchronized (this.taskThreads) {
            this.taskThreads.addLast(task);
            this.taskThreads.notifyAll();
        }
    }


    /**
     * 初始化操作
     */
    private void init() {
        for (int i = 0; i < this.size; i++) {
            createWorker();
        }
    }

    /**
     * 创建工作线程
     */
    private void createWorker() {
        WorkThread workThread = new WorkThread(threadGroup,getWorkThreadName(), this.taskThreads);
        this.workThreads.add(workThread);
        workThread.start();
    }


    /**
     * 默认规则生成线程名称
     *
     * @return
     */
    private synchronized String getWorkThreadName() {
        return THREAD_POOL_WORK_THREAD_NAME_PREFIX + seq.getAndIncrement();
    }

}
