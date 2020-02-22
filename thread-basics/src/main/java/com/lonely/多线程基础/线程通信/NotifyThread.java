package com.lonely.多线程基础.线程通信;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/15 14:30
 * Description: 线程唤醒测试类
 */
public class NotifyThread {

    private static final Object LOCK = new Object();

    public static void main(String[] args) {

        //1.验证执行notify()必须持有锁
        //checkBeHaveLock();

        //2.验证wait和notify必须使用同一把锁
        checkWaitAndNotifyIsIdenticalLock();

    }

    /**
     * 验证wait和notify必须使用同一把锁
     */
    private static void checkWaitAndNotifyIsIdenticalLock(){
        Object newLock = new Object();

        new Thread(()->{
            synchronized (LOCK){
                try {
                    System.out.println(MessageFormat.format("{0}锁住了", Thread.currentThread().getName()));
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(MessageFormat.format("{0}锁被释放", Thread.currentThread().getName()));
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //这种是错误的
        /*synchronized (newLock){
            newLock.notify();
        }*/

        synchronized (LOCK){
            LOCK.notify();
        }

    }


    /**
     * 验证必须持有锁
     */
    private static void checkBeHaveLock(){
        new Thread(()->{
            synchronized (LOCK){
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //休眠1s后唤醒
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOCK.notify();

    }

}
