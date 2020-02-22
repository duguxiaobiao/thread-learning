package com.lonely.多线程基础.线程通信;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/15 13:45
 * Description: wait使用demo
 */
public class WaitThread {


    public static void main(String[] args) throws InterruptedException {
        Object LOCK = new Object();


        new Thread(()->{
            try {
                synchronized (LOCK){
                    System.out.println(Thread.currentThread().getName());
                    //休眠一秒后释放锁
                    TimeUnit.SECONDS.sleep(1);
                    LOCK.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                synchronized (LOCK){
                    System.out.println(Thread.currentThread().getName());
                    //休眠一秒后释放锁
                    TimeUnit.SECONDS.sleep(1);
                    LOCK.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        TimeUnit.SECONDS.sleep(2);
        System.out.println("主线程执行结束");
    }

}
