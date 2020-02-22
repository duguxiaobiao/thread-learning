package com.lonely.多线程基础.锁.手写锁实现;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @auther: 15072
 * @date: 2020/2/16 13:21
 * Description: 自定义锁实现---版本3实现  优化：等待指定时间后，若还没获取到锁，则不在尝试获取锁。
 */
public class CustomerLockVersion3 implements CustomerLockInterface {

    /**
     * 锁状态 true：已经存在锁被占用  false:还没占用锁资源
     */
    private boolean lockState = false;

    /**
     * 阻塞等待锁的线程集合
     */
    private List<Thread> blockThreads = new ArrayList<>();

    /**
     * 保存获取到锁的线程
     */
    private Thread currentThread;


    /**
     * 加锁
     */
    @Override
    public synchronized void lock() {

        while (this.lockState) {
            //锁已被占用，进入等待队列
            System.out.println(MessageFormat.format("{0}未获取到锁，进入等待队列中", Thread.currentThread().getName()));
            this.blockThreads.add(Thread.currentThread());
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //唤醒后，从阻塞队列中移除
            this.blockThreads.remove(Thread.currentThread());
        }

        //锁没有被占用
        this.lockState = true;
        this.currentThread = Thread.currentThread();
        System.out.println(MessageFormat.format("{0}获取到锁", Thread.currentThread().getName()));
    }

    /**
     * 尝试获取锁，指定时间未获取到锁，则返回false
     *
     * @param timeout
     */
    @Override
    public synchronized boolean tryLock(long timeout) {

        if (timeout < 0) {
            throw new RuntimeException("tryLock入参超时时间不能小于0");
        }

        long startTime = System.currentTimeMillis();
        long resultTime = timeout + startTime;

        while (this.lockState) {

            //判断是否超时
            long waitTime = resultTime - System.currentTimeMillis();
            if (waitTime <= 0) {
                //超时，直接返回
                System.out.println(MessageFormat.format("{0}获取锁超时", Thread.currentThread().getName()));
                return false;
            }

            //锁已被占用，进入等待队列
            System.out.println(MessageFormat.format("{0}未获取到锁，进入等待队列中", Thread.currentThread().getName()));
            this.blockThreads.add(Thread.currentThread());
            try {
                this.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //唤醒后，从阻塞队列中移除
            this.blockThreads.remove(Thread.currentThread());
        }

        //锁没有被占用
        this.lockState = true;
        this.currentThread = Thread.currentThread();
        System.out.println(MessageFormat.format("{0}获取到锁", Thread.currentThread().getName()));

        return true;
    }


    /**
     * 解锁
     */
    @Override
    public synchronized void unLock() {
        System.out.println(MessageFormat.format("{0}尝试释放锁", Thread.currentThread().getName()));
        if (Thread.currentThread() == currentThread) {
            this.lockState = false;
            this.currentThread = null;
            System.out.println(MessageFormat.format("{0}释放了锁", Thread.currentThread().getName()));
            this.notifyAll();
        } else {
            System.out.println(MessageFormat.format("{0}线程非占用锁线程，释放锁失败，忽略该操作", Thread.currentThread().getName()));
        }
    }

    /**
     * 获取阻塞的等待锁线程集合
     *
     * @return
     */
    @Override
    public Collection<Thread> getBlockThreads() {
        return Collections.unmodifiableList(this.blockThreads);
    }

    /**
     * 获取阻塞等待锁的线程集合的长度
     *
     * @return
     */
    @Override
    public int getBlockThreadsSize() {
        return this.blockThreads.size();
    }
}
