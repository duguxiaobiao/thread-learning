package com.lonely.juc.循环栅栏;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @auther: 15072
 * @date: 2020/3/2 19:27
 * Description: 循环栅栏----当多个线程都准备完毕后，在同时进行，跟CountDownLatch很类似，
 * 但是CountDownLatch相当于有一个裁判员角色，由裁判员来统计是否人员都到齐了
 * 而 CyclicBarrier则取消了裁判员角色，有各个线程自己判断，都觉得到齐了就可以开始了，
 * 一般用于某个多个线程正在running期间，暂停等待，等条件满足后，在一起运行
 */
public class CyclicBarrierTest {


    public static void main(String[] args) {
        //初始化，如果设置了3个，但是只有两个线程参与，那么那两个线程会一直等待下去，直到清零或者中断它
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                System.out.println(MessageFormat.format("线程：{0}start", Thread.currentThread().getName()));
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println(MessageFormat.format("线程{0}等待中.....", Thread.currentThread().getName()));

                    //一直等待中，等其余线程都准备好了，就开始继续运行了
                    cyclicBarrier.await();

                    //等待指定市场，超过了，不再等待,如果超过了，超时的线程抛出TimeoutException异常，正在等待的线程抛出BrokenBarrierException异常
                    //cyclicBarrier.await(1, TimeUnit.SECONDS);

                    System.out.println(MessageFormat.format("线程{0}再次运行中.....", Thread.currentThread().getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        //添加监控
        while (true) {
            //获取正在等待的线程数量
            int numberWaiting = cyclicBarrier.getNumberWaiting();

            //初始传入的计数数量
            int parties = cyclicBarrier.getParties();

            //判断内存屏障是否失效，如果有一个或者多个中断或者超时，就会导致内存屏障失效， 此时为true，否则为false
            boolean broken = cyclicBarrier.isBroken();

            System.out.println(MessageFormat.format("总计数：{0}个，正在等待的线程数：{1}个，当前内存屏障是否失效：{2}",
                    parties, numberWaiting, broken));

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
