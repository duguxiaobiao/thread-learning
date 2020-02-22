package com.lonely.多线程基础.锁.手写锁实现;


import java.util.Collection;

/**
 * 自定义锁接口
 */
public interface CustomerLockInterface {

    /**
     * 加锁
     */
    void lock();

    /**
     * 尝试获取锁，指定时间未获取到锁，则返回false
     *
     * @param timeout
     */
    boolean tryLock(long timeout);

    /**
     * 解锁
     */
    void unLock();

    /**
     * 获取阻塞的等待锁线程集合
     *
     * @return
     */
    Collection<Thread> getBlockThreads();

    /**
     * 获取阻塞等待锁的线程集合的长度
     *
     * @return
     */
    int getBlockThreadsSize();

}
