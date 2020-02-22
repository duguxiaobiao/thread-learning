package com.lonely.多线程基础.锁.手写锁实现;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @auther: 15072
 * @date: 2020/2/16 13:21
 * Description: 自定义锁实现---版本1实现  基础功能实现，存在非获取锁线程释放锁导致线程安全问题
 */
public class CustomerLockVersion1 implements CustomerLockInterface {

    /**
     * 锁状态 true：已经存在锁被占用  false:还没占用锁资源
     */
    private boolean lockState = false;

    /**
     * 阻塞等待锁的线程集合
     */
    private List<Thread> blockThreads = new ArrayList<>();


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
        System.out.println(MessageFormat.format("{0}获取到锁", Thread.currentThread().getName()));
    }

    /**
     * 尝试获取锁，指定时间未获取到锁，则返回false
     *
     * @param timeout
     */
    @Override
    public boolean tryLock(long timeout) {
        return false;
    }


    /**
     * 解锁
     */
    @Override
    public synchronized void unLock() {
        this.lockState = false;
        System.out.println(MessageFormat.format("{0}释放了锁", Thread.currentThread().getName()));
        this.notifyAll();
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
