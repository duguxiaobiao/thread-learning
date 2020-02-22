package com.lonely.多线程基础.线程中断;

/**
 * @auther: 15072
 * @date: 2020/2/13 14:52
 * Description: 基于 新建状态或者死亡状态的线程的 线程中断场景
 * 结论：针对new状态或terminated状态的线程，线程中断是与其无效的。
 */
public class ThreadInterruptByNewAndTerminated {

    public static void main(String[] args) throws InterruptedException {

        //new状态的线程
        Thread newThread = new Thread();
        System.out.println(newThread.getState());

        //结束状态的线程
        Thread terminatedThread = new Thread();
        terminatedThread.start();
        terminatedThread.join();
        System.out.println(terminatedThread.getState());

        //现在对new状态和terminated状态的线程进行线程中断
        newThread.interrupt();
        terminatedThread.interrupt();

        System.out.println("主线程结束");

    }


}
