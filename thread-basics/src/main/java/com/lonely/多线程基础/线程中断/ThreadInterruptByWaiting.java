package com.lonely.多线程基础.线程中断;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/13 15:20
 * Description: 基于 等待状态（Thread，或者wait,join）的线程中断场景
 * 结论：在线程中，方法 sleep，join，wait等方法必须处理中断异常，即可以监听到中断信号
 */
public class ThreadInterruptByWaiting {

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            while (true) {
                System.out.println("t1执行中....");
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("t1线程中断离开");
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("t1线程中断拦截");
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread t2 = new Thread(new WaitThread());

        t1.start();
        t2.start();

        t1.interrupt();
        t2.interrupt();

        System.out.println(MessageFormat.format("t1[STATE:{0}]----,[interrupt:{1}]",t1.getState(),t1.isInterrupted()) );
        System.out.println(MessageFormat.format("t2[STATE:{0}]----,[interrupt:{1}]",t2.getState(),t2.isInterrupted()) );

        System.out.println("主线程结束");


    }


    private static class WaitThread implements Runnable{

        @Override
        public void run() {
            synchronized (this){
                try {
                    System.out.println("t2线程执行");
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("t2线程中断已拦截");
                }
            }
        }
    }



}
