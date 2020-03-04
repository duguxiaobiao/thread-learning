package com.lonely.juc.信号量;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/3/2 20:22
 * Description: 信号量，用处很大，可以用于限流等等之类，假设初始化时制定了固定长度的令牌数，每个使用他的线程可以决定拿走多少个令牌，当
 * 令牌清空后，其余线程进入后就会进行阻塞，知道其余线程释放了令牌，有多余的令牌后，其余线程才可以拿到令牌使用。也可以称之为
 * 通行证，达到同步的效果。传统的synchronized只能保证同时1个线程进入执行，而semaphore可以让多个线程拥有权限进入并且执行。
 */
public class SemaphoreTest {


    public static void main(String[] args) {
        //创建并且设定5个资源令牌
        Semaphore semaphore = new Semaphore(5);

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(MessageFormat.format("线程：{0} start", Thread.currentThread().getName()));
                try {
                    //从池子中拿走1个，这种方式的，在运行期间可以被打断
                    semaphore.acquire();
                    //也可以一次性拿走两个
                    //semaphore.acquire(2);
                    //也可以同时将全部令牌都拿走
                    //semaphore.drainPermits();

                    //这种方式，不可被打断,这也是与 acquire()的区别
                    //semaphore.acquireUninterruptibly();

                    System.out.println(MessageFormat.format("线程：{0} 拿到了令牌", Thread.currentThread().getName()));
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //放回令牌，当然也有配套的同时放回多个的重载方法
                    semaphore.release();
                    System.out.println(MessageFormat.format("线程：{0} 执行结束，放回了令牌", Thread.currentThread().getName()));
                }
            }).start();
        }

    }


}
