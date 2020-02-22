package com.lonely.设计模式在多线程中的体现.多线程读写锁分离;

import java.text.MessageFormat;

/**
 * @auther: 15072
 * @date: 2020/2/22 19:40
 * Description: 自定义读写锁实现
 */
public class ReadAndWriteLock {

    /**
     * 正在读的线程数
     */
    private int readingReaders;

    /**
     * 正在等着读的线程数
     */
    private int waitingReaders;

    /**
     * 正在执行写操作的线程数
     */
    private int writingWriters;

    /**
     * 正在等着写操作的线程数
     */
    private int waitingWriters;


    private static final Object OBJECT_LOCK = new Object();


    /**
     * 读加锁
     */
    public void readLock() throws InterruptedException {
        synchronized (OBJECT_LOCK) {
            this.waitingReaders++;
            try {
                while (this.writingWriters > 0) {
                    OBJECT_LOCK.wait();
                }
                this.readingReaders++;
            } finally {
                this.waitingReaders--;
            }

        }

    }

    /**
     * 读解锁
     */
    public void readUnLock() {
        //System.out.println(MessageFormat.format("{0}{1}{2}{3}", this.readingReaders, this.waitingReaders, this.writingWriters, this.waitingWriters));
        synchronized (OBJECT_LOCK) {
            this.readingReaders--;
            OBJECT_LOCK.notifyAll();
        }
    }


    /**
     * 写加锁
     */
    public void writeLock() throws InterruptedException {
        synchronized (OBJECT_LOCK) {
            this.waitingWriters++;
            try {
                while (this.readingReaders > 0 || this.writingWriters > 0) {
                    //不管是存在正在读的还是正在写的，都得等
                    OBJECT_LOCK.wait();
                }
                this.writingWriters++;
            } finally {
                this.waitingWriters--;
            }

        }
    }

    /**
     * 写解锁
     */
    public void writeUnLock() {
        synchronized (OBJECT_LOCK) {
            this.writingWriters--;
            OBJECT_LOCK.notifyAll();
        }
    }


}
