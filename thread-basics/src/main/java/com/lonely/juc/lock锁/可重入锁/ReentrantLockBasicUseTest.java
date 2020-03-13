package com.lonely.juc.lock锁.可重入锁;

import org.junit.Test;

import java.sql.Time;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther: 15072
 * @date: 2020/3/4 13:13
 * Description: 可重入锁 api使用测试
 * 总结： 1. 使用Lock时，注意规范，使用try-finally范式，保证锁能够在异常情况也能释放掉
 * 2. lock.lock()加锁方式，是无法被中断的，而lock.lockInterruptibly()或者tryLock()方式是可以被中断的
 * 3. lock和synchronized对比：lock()和unLock() 相比synchronized的进入和退出同步指令，而Object.wait()和notify()好比Lock的
 * Condition.await()和Condition.signal(),只是lock和condition更加灵活
 */
public class ReentrantLockBasicUseTest {

    public static void main(String[] args) throws InterruptedException {
        testBasicUsed();
    }

    //模拟基本使用
    public static void testBasicUsed() throws InterruptedException {
        //默认是非公平锁
        ReentrantLock lock = new ReentrantLock();

        //使用公平锁，保证多个线程的cpu调度上(获取到锁机会)相对平均一点
        //lock = new ReentrantLock(true);
        ReentrantLockThread t1 = new ReentrantLockThread(lock);
        t1.start();

        ReentrantLockThread t2 = new ReentrantLockThread(lock);
        t2.start();

        TimeUnit.MILLISECONDS.sleep(100);
        //可以发现中断不了，如果想要中断，可以使用另一种加锁方式
        //t2.interrupt();

        //添加监控
        while (true) {
            //返回正在等待lock锁的线程数量
            int queueLength = lock.getQueueLength();

            //返回是否存在等待锁的线程
            boolean hasQueuedThreads = lock.hasQueuedThreads();

            //判断当前lock是否是公平锁
            boolean fair = lock.isFair();

            //判断是否存在线程持有锁
            boolean locked = lock.isLocked();

            System.out.println(MessageFormat.format("当前Lock中是否存在正在等待lock锁的线程：{0},是否是公平锁：{1}，是否有线程持有锁：{2}",
                    hasQueuedThreads, fair, locked));

            TimeUnit.SECONDS.sleep(1);
        }
    }


    static class ReentrantLockThread extends Thread {
        private ReentrantLock lock;

        ReentrantLockThread(ReentrantLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            //使用Lock类，标准范式 try()finally(),确保锁能够释放
            try {
                //使用lock()方法，会一直阻塞，无法中断
                //lock.lock();
                //可中断锁方法，可以被中断
                lock.lockInterruptibly();

                //返回当前线程持有当前lock锁的数量，如果已经解锁，则应该为0
                int holdCount = lock.getHoldCount();

                //当前线程是否持有锁
                boolean heldByCurrentThread = lock.isHeldByCurrentThread();

                System.out.println(MessageFormat.format("线程：{0}输出=====当前线程持有锁数量：{1}，当前线程是否持有锁：{2}",
                        Thread.currentThread().getName(), holdCount, heldByCurrentThread));
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
