package com.lonely.多线程基础.线程池.手写线程池实现;

import com.lonely.多线程基础.线程池.手写线程池实现.拒绝策略.DefaultRefusalStrategy;
import com.lonely.多线程基础.线程池.手写线程池实现.拒绝策略.RefusalStrategy;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther: 15072
 * @date: 2020/2/17 12:30
 * Description: 自定义线程池----版本2：在版本1的基础上添加了线程监控，扩容，缩容，以及关闭线程池
 */
public class CustomerThreadPool2 extends Thread {

    /**
     * 默认任务队列长度长度
     */
    private static final int DEFAULT_TASK_QUEUE_MAX_SIZE = 50;

    /**
     * 线程池对应的线程组
     */
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

    /**
     * 核心线程数
     */
    private int coreSize;

    /**
     * 最大线程数
     */
    private int maxSize;

    /**
     * 活跃线程数
     */
    private int activeSize;

    /**
     * 最大任务队列长度
     */
    private int maxTaskQueueSize;

    /**
     * 拒绝策略
     */
    private RefusalStrategy refusalStrategy;

    /**
     * 线程池状态
     */
    private volatile boolean isShutDown;

    public CustomerThreadPool2(int coreSize, int maxSize) {
        this(coreSize, maxSize, DEFAULT_TASK_QUEUE_MAX_SIZE, new DefaultRefusalStrategy());
    }

    public CustomerThreadPool2(int coreSize, int maxSize, int maxTaskQueueSize, RefusalStrategy refusalStrategy) {
        if (coreSize > maxSize) {
            maxSize = coreSize;
        }

        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.maxTaskQueueSize = maxTaskQueueSize;
        this.refusalStrategy = refusalStrategy;

        this.init();
    }

    /**
     * 监控状态
     */
    @Override
    public void run() {
        while (!isShutDown) {

            //输出线程状态
            System.out.println(MessageFormat.format("核心线程数：{0},最大线程数：{1},任务队列数：{2}，活跃线程数：{3}",
                    this.coreSize, this.maxSize, this.taskThreads.size(), this.activeSize));

            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this.workThreads) {
                //判断是否需要扩容,任务数量>活跃数
                if (this.taskThreads.size() > this.activeSize && this.activeSize < this.maxSize) {
                    //还需要扩，扩多少 = max - active
                    System.out.println("================需要扩容");
                    for (int i = this.activeSize; i < this.maxSize; i++) {
                        this.createWorker();
                    }
                    this.activeSize = this.maxSize;
                }

                //判断是否需要缩容,如果没有待执行的任务了，且互动线程>核心线程，则缩容
                if (this.taskThreads.isEmpty() && this.activeSize > this.coreSize) {
                    System.out.println("================需要缩容");

                    int releaseSize = this.activeSize - this.coreSize;
                    for (Iterator<WorkThread> iterator = this.workThreads.iterator(); iterator.hasNext(); ) {
                        if (releaseSize <= 0) {
                            break;
                        }
                        WorkThread next = iterator.next();
                        if (next.getThreadState() != ThreadStateEnum.RUNNING) {
                            next.interrupt();
                            iterator.remove();
                            releaseSize--;
                            this.activeSize--;
                        }
                    }
                }

            }


        }

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

        if (this.isShutDown) {
            throw new RuntimeException("线程池已关闭，不能再提交任务");
        }

        synchronized (this.taskThreads) {

            //1. 如果当前的任务队列满了，则不在接收任务
            if (this.taskThreads.size() >= this.maxTaskQueueSize) {
                //满了，使用拒绝策略拒绝
                this.refusalStrategy.refuse();
            }
            this.taskThreads.addLast(task);
            this.taskThreads.notifyAll();
        }
    }


    /**
     * 关闭线程池
     */
    public void shutDown() {
        System.out.println("正在关闭线程池");
        this.isShutDown = true;
        synchronized (this.workThreads) {
            while (!this.taskThreads.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (getRunningSizes() > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //没有运行中线程了，则销毁资源
            for (WorkThread workThread : this.workThreads) {
                workThread.interrupt();
                this.activeSize--;
            }
        }
        System.out.println("线程池成功关闭");
        System.out.println(MessageFormat.format("核心线程数：{0},最大线程数：{1},任务队列数：{2}，活跃线程数：{3}",
                this.coreSize, this.maxSize, this.taskThreads.size(), this.activeSize));
    }


    private Long getRunningSizes() {
        synchronized (this.workThreads) {
            return this.workThreads.stream().filter(x -> x.getThreadState() == ThreadStateEnum.RUNNING).count();
        }
    }


    /**
     * 初始化操作
     */
    private void init() {
        for (int i = 0; i < this.coreSize; i++) {
            createWorker();
        }
        this.activeSize = this.coreSize;

        this.start();
    }

    /**
     * 创建工作线程
     */
    private void createWorker() {
        WorkThread workThread = new WorkThread(threadGroup, getWorkThreadName(), this.taskThreads);
        this.workThreads.add(workThread);
        workThread.start();
        this.activeSize++;
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
