package com.lonely.juc.基于cas实现简易锁;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/28 19:36
 * Description:
 */
public class MainTest {

    public static void main(String[] args) {
        TryLockForCAS lockForCAS = new TryLockForCAS();

        //尝试获取锁场景
        /*for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                boolean tryLock = false;
                try {
                    tryLock = lockForCAS.tryLock();
                    if (!tryLock) {
                        System.out.println(MessageFormat.format("{0}没有获取到锁，执行其他事情", Thread.currentThread().getName()));
                        return;
                    }
                    //获取到了锁
                    System.out.println(MessageFormat.format("{0}任务进行中", Thread.currentThread().getName()));
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(MessageFormat.format("{0}任务结束", Thread.currentThread().getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (tryLock) {
                        lockForCAS.unLock();
                    }
                }
            }).start();
        }*/


        //必须获取锁场景
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    lockForCAS.lock();
                    //获取到了锁
                    System.out.println(MessageFormat.format("{0}任务进行中", Thread.currentThread().getName()));
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(MessageFormat.format("{0}任务结束", Thread.currentThread().getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lockForCAS.unLock();
                }
            }).start();
        }


    }
}
