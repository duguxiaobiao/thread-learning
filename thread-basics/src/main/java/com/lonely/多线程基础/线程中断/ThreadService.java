package com.lonely.多线程基础.线程中断;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/13 17:29
 * Description: 线程中断工具类
 */
public class ThreadService {

    /**
     * 执行线程
     */
    private Thread executThread;

    private volatile boolean flag = false;

    public void submit(Runnable task) {
        //每次实例化一个线程，什么事情都不做
        this.executThread = new Thread(() -> {
            //创建一个守护线程
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("接收到中断指令，中断");
            }
            this.flag = true;

        });
        this.executThread.start();


    }


    public void shutDown() {
        this.shutDown(-1);
    }

    public void shutDown(long outTime) {

        if (outTime <= 0) {
            //没有设置超时时间，直接中断
            executThread.interrupt();
            System.out.println("接收shutdown指令,中断服务");
        } else {

            //等待超时
            long currTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - currTime <= outTime) {
                if (this.flag) {
                    return;
                }
                //未超时,空循环
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //已超时
            this.executThread.interrupt();
            System.out.println("接收shuntdown指定，已超时，中断服务");
        }

        this.flag = false;

    }
}
