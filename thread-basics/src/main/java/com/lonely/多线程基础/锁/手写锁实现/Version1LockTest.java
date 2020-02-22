package com.lonely.多线程基础.锁.手写锁实现;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @auther: 15072
 * @date: 2020/2/16 13:32
 * Description: 版本1锁实现测试类
 */
public class Version1LockTest {


    public static void main(String[] args) {

        CustomerLockVersion1 customerLockVersion1 = new CustomerLockVersion1();

        Stream.of("线程A", "线程B").forEach(x -> {
            new Thread(() -> {
                try {
                    //占锁
                    customerLockVersion1.lock();

                    //模拟执行操作
                    try {
                        System.out.println(MessageFormat.format("{0}执行操作中", Thread.currentThread().getName()));
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(MessageFormat.format("{0}任务执行结束", Thread.currentThread().getName()));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    customerLockVersion1.unLock();
                }
            }, x).start();
        });

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerLockVersion1.unLock();

    }

}
