package com.lonely.juc.计数器;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @auther: 15072
 * @date: 2020/3/2 17:01
 * Description: 计时器测试：---- 提供指定数量的令牌，每次调用countDown()会消耗一个令牌，当令牌没有用完时，
 *                                  可以使用 await()阻塞来等令牌使用完，令牌使用完后，就不再阻塞了，开始运行
 *                                   一般用于等待多个线程执行完毕后，其余线程开始操作，之前可以使用join来实现
 */
public class CountDownLatchTest {


    /**
     *
     * @throws InterruptedException
     */
    @Test
    public void testCreateAndGetAndCountDown() throws InterruptedException {

        //测试创建方式,传入计数器数量
        CountDownLatch countDownLatch = new CountDownLatch(5);

        //测试 获取计时器数量,这是实时数据
        assertEquals(countDownLatch.getCount(),5);

        //测试 countDown方法，即当前计数器数量 -1
        countDownLatch.countDown();
        assertEquals(countDownLatch.getCount(),4);

    }

    /**
     * 测试 await()会阻塞,即等待计数器清零后，停止阻塞
     */
    @Test
    public void testAwaitIsBlock() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(()->{
            System.out.println("thread1 start");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println("thread1 end");
        }).start();

        countDownLatch.await();

        System.out.println("test thread over");
    }

    /**
     * 测试 await(long timeout);测试一段时候后超时是什么场景---超时不在等待，跟子线程同步进行
     */
    @Test
    public void testAwaitTimeOut() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(()->{
            System.out.println("thread1 start");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println("thread1 end");
        }).start();

        try {
            countDownLatch.await(1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("超时了，不再等待");
        }

        System.out.println("test thread over");
    }


}
