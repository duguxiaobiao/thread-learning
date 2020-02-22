package com.lonely.多线程基础.线程中断;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/13 14:56
 * Description: 基于 运行中状态的线程中断 场景
 */
public class ThreadInterruptByRunning {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("子线程输出。。。。");
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("收到中断指令");
                    break;
                }
            }
        });
        thread.start();

        TimeUnit.SECONDS.sleep(1);

        thread.interrupt();
        System.out.println(thread.isInterrupted());
        System.out.println(thread.getState());

        System.out.println("主线程结束");


    }


}
