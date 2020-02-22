package com.lonely.多线程基础.线程池.手写线程池实现;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @auther: 15072
 * @date: 2020/2/17 12:35
 * Description: 线程池工作线程对象
 */
public class WorkThread extends Thread {

    private LinkedList<Runnable> taskThreads;

    private ThreadStateEnum threadState = ThreadStateEnum.FREE;

    public WorkThread(ThreadGroup threadGroup,String workThreadName, LinkedList<Runnable> taskThread) {
        super(threadGroup,workThreadName);
        this.taskThreads = Optional.ofNullable(taskThread).orElse(new LinkedList<>());
    }

    @Override
    public void run() {
        OUTER:
        while (true) {
            while (threadState == ThreadStateEnum.FREE) {
                Runnable runnable;
                synchronized (this.taskThreads) {
                    while (this.taskThreads.isEmpty()) {
                        try {
                            this.threadState = ThreadStateEnum.BLOCKED;
                            this.taskThreads.wait();
                        } catch (InterruptedException e) {
                            //中断后退出
                            this.threadState = ThreadStateEnum.CLOSE;
                            break OUTER;
                        }
                    }

                    //有资源,拿出来执行
                    runnable = this.taskThreads.removeFirst();
                }

                if (runnable != null) {
                    this.threadState = ThreadStateEnum.RUNNING;
                    runnable.run();
                    this.threadState = ThreadStateEnum.FREE;
                }

            }
        }
    }

    public ThreadStateEnum getThreadState() {
        return threadState;
    }
}
