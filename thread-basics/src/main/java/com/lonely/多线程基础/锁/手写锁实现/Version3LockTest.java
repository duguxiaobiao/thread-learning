package com.lonely.多线程基础.锁.手写锁实现;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @auther: 15072
 * @date: 2020/2/16 13:32
 * Description: 版本3锁实现测试类,验证获取锁操作优化
 */
public class Version3LockTest {


    public static void main(String[] args) {

        CustomerLockVersion3 customerLockVersion3 = new CustomerLockVersion3();
        Stream.of("线程A", "线程B").forEach(x -> {
            new Thread(() -> {
                boolean tryLock = false;
                try {
                    //占锁
                    tryLock = customerLockVersion3.tryLock(2_000);
                    if (tryLock) {
                        //获取到了锁，进行任务
                        //模拟执行操作
                        try {
                            System.out.println(MessageFormat.format("{0}执行操作中", Thread.currentThread().getName()));
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println(MessageFormat.format("{0}任务执行结束", Thread.currentThread().getName()));
                    } else {
                        //超时未获取到锁
                        System.out.println(MessageFormat.format("{0}:超时未获取锁，执行通知任务", Thread.currentThread().getName()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    if(tryLock){
                        customerLockVersion3.unLock();
                    }
                }
            }, x).start();
        });

    }

}
