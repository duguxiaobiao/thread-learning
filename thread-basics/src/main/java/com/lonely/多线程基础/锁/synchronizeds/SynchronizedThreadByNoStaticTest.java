package com.lonely.多线程基础.锁.synchronizeds;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/14 13:46
 * Description: 实例对象(非静态对象)同步代码块和同步方法的使用
 */
public class SynchronizedThreadByNoStaticTest {

    public static void main(String[] args) {

        TicketWindow ticketWindow = new TicketWindow();
        Thread t1 = new Thread(ticketWindow, "1号窗口");
        Thread t2 = new Thread(ticketWindow, "2号窗口");
        Thread t3 = new Thread(ticketWindow, "3号窗口");

        t1.start();
        t2.start();
        t3.start();

    }

    /**
     * 叫号窗口
     */
    private static class TicketWindow implements Runnable {

        public static final int MAX = 100;

        private int index = 0;

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                //随机产品0到1的整数，即要么是0 要么是1，这样就可以同时使用同步函数以及同步代码块了
                //还可以验证实例对象的同步方法使用的就是this锁。
                int i = random.nextInt(2);
                System.out.println(i);
                if (i % 2 == 0) {
                    if (!getTicket()) {
                        break;
                    }
                } else {
                    if (!getTicketByBlock()) {
                        break;
                    }

                }

            }
        }

        /**
         * 使用同步方法
         *
         * @return
         */
        public synchronized boolean getTicket() {
            if (index < MAX) {
                System.out.println(MessageFormat.format("{0}打印第：{1}号票", Thread.currentThread().getName(), ++index));
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        }

        /**
         * 使用同步代码块来实现
         *
         * @return
         */
        public boolean getTicketByBlock() {
            synchronized (this) {
                if (index < MAX) {
                    System.out.println(MessageFormat.format("{0}打印第：{1}号票", Thread.currentThread().getName(), ++index));
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }

    }
}
