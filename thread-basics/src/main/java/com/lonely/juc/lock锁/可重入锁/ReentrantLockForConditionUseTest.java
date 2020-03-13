package com.lonely.juc.lock锁.可重入锁;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther: 15072
 * @date: 2020/3/4 17:59
 * Description: reentrantlock的condition使用--实现生产者消费者模式
 */
public class ReentrantLockForConditionUseTest {

    /**
     * 生产的最大限制，超过限制就不在生产
     */
    private static final int MAX_SIZE = 10;

    /**
     * 随机休眠
     */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 定义重入锁
     */
    private static Lock lock = new ReentrantLock();

    /**
     * 生产者对应的condition
     */
    private static Condition producerCondition = lock.newCondition();

    /**
     * 消费者对应的condition
     */
    private static Condition consumerCondition = lock.newCondition();

    public static void main(String[] args) {

        //5个生产者线程
        for (int i = 0; i < 5; i++) {
            new ProducerThread(lock).start();
        }

        //10个消费者
        for (int i = 0; i < 10; i++) {
            new ConsumerThread(lock).start();
        }


    }


    static class ProducerThread extends Thread {

        private Lock lock;

        ProducerThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    lock.lock();

                    while (atomicInteger.get() > MAX_SIZE) {
                        //有数据，休息
                        producerCondition.await();
                    }

                    //没数据了，进行生产
                    int incrementAndGet = atomicInteger.incrementAndGet();
                    System.out.println(MessageFormat.format("生产者线程：{0}生产了数据：{1}", Thread.currentThread().getName(), incrementAndGet));

                    //模拟过程，休眠
                    TimeUnit.MILLISECONDS.sleep(RANDOM.nextInt(2000));

                    //通知消费者消费
                    consumerCondition.signalAll();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }


            }
        }
    }


    static class ConsumerThread extends Thread {
        private Lock lock;

        ConsumerThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    lock.lock();

                    while (atomicInteger.get() == 0) {
                        //没数据消费了，休息
                        consumerCondition.await();
                    }

                    //有数据了，进行消费
                    int decrementAndGet = atomicInteger.getAndDecrement();
                    System.out.println(MessageFormat.format("消费者线程：{0}消费了数据：{1}", Thread.currentThread().getName(), decrementAndGet));

                    //模拟过程，休眠
                    TimeUnit.MILLISECONDS.sleep(RANDOM.nextInt(2000));

                    producerCondition.signalAll();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }


        }
    }
}
