package com.lonely.juc.基于cas实现简易锁;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther: 15072
 * @date: 2020/2/28 18:27
 * Description: 基于cas实现锁
 */
public class TryLockForCAS {

    private AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 获取到锁的线程
     */
    private Thread currentThread;

    /**
     * 获取锁，没获取到锁的线程循环
     *
     * @throws InterruptedException
     */
    public void lock() throws InterruptedException {
        while (!this.atomicInteger.compareAndSet(0, 1)) {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        //获取到了锁
        System.out.println(MessageFormat.format("{0}获取到了锁", Thread.currentThread().getName()));
        this.currentThread = Thread.currentThread();
    }


    /**
     * 尝试获取锁
     *
     * @return
     */
    public boolean tryLock() {
        boolean compareAndSet = atomicInteger.compareAndSet(0, 1);
        if (!compareAndSet) {
            return false;
        }
        this.currentThread = Thread.currentThread();
        System.out.println(MessageFormat.format("{0}获取到了锁", Thread.currentThread().getName()));
        return true;
    }

    /**
     * 解锁
     */
    public void unLock() {
        if (Thread.currentThread() != this.currentThread) {
            return;
        }

        boolean compareAndSet = this.atomicInteger.compareAndSet(1, 0);
        if (!compareAndSet) {
            throw new RuntimeException("解锁失败");
        }
        this.currentThread = null;
    }

}
