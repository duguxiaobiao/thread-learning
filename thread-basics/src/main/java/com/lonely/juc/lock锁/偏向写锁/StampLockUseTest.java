package com.lonely.juc.lock锁.偏向写锁;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

/**
 * @auther: 15072
 * @date: 2020/3/4 20:21
 * Description:
 */
public class StampLockUseTest {

    private static final StampedLock stampedLock = new StampedLock();

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Thread pessimismrReadThread = new Thread(() -> {
            while (true) {
                //pessimismrRead();
                optimisticRead();
            }
        });

        Thread writeThread = new Thread(() -> {
            while (true) {
                write();
            }
        });

        for (int i = 0; i < 8; i++) {
            executorService.execute(pessimismrReadThread);
        }

        executorService.execute(writeThread);
        executorService.execute(writeThread);
    }


    /**
     * 相对写操作的悲观读
     */
    private static void pessimismrRead() {
        long stamp = -1;
        try {
            stamp = stampedLock.readLock();

            System.out.println(MessageFormat.format("线程：{0}输出内容：{1}",
                    Thread.currentThread().getName(), atomicInteger.get()));

            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }


    /**
     * 相对写操作的了乐观读
     */
    private static void optimisticRead() {
        long stamp = stampedLock.tryOptimisticRead();
        if (stampedLock.validate(stamp)) {
            try {
                stamp = stampedLock.readLock();
                System.out.println(MessageFormat.format("线程：{0}输出内容：{1}",
                        Thread.currentThread().getName(), atomicInteger.get()));

                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }


    }

    /**
     * 写操作
     */
    private static void write() {
        long stamp = -1;
        try {
            stamp = stampedLock.writeLock();

            int incrementAndGet = atomicInteger.incrementAndGet();
            System.out.println(MessageFormat.format("写线程：{0}写入数据：{1}",
                    Thread.currentThread().getName(), incrementAndGet));

            TimeUnit.SECONDS.sleep(1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }


}
