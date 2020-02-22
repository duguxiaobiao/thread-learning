package com.lonely.多线程基础.线程通信;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther: 15072
 * @date: 2020/2/15 15:17
 * Description: 生产者消费者模型实例
 * 需求：多生产者，多消费者，每次最多生产5个，最多消费1个
 */
public class ProducerAndConsumerThread {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(new Producer(ticket), "生产者A").start();
        new Thread(new Producer(ticket), "生产者B").start();

        new Thread(new Consumer(ticket), "消费者A").start();
        new Thread(new Consumer(ticket), "消费者B").start();
        new Thread(new Consumer(ticket), "消费者C").start();
    }


    private static class Producer implements Runnable {

        private Ticket ticket;

        public Producer(Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (ticket) {
                    if (ticket.tickets.get() <= 0) {
                        //没票了，需要生产5个
                        try {
                            ticket.tickets.addAndGet(5);
                            TimeUnit.SECONDS.sleep(1);
                            System.out.println(MessageFormat.format("{0}生产了5个，目前有：{1}个", Thread.currentThread().getName(), ticket.tickets.get()));
                            ticket.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //有票，去通知消费者
                    ticket.notifyAll();
                }
            }

        }
    }


    private static class Consumer implements Runnable {

        private Ticket ticket;

        public Consumer(Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (ticket) {
                    while (ticket.tickets.get() <= 0) {
                        //没票，等待
                        try {
                            ticket.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //有票，消费去
                    ticket.tickets.decrementAndGet();
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                        System.out.println(MessageFormat.format("{0}消费了票，目前还剩：{1}张",
                                Thread.currentThread().getName(), ticket.tickets.get()));
                        //消费完后去唤醒其余的消费者
                        ticket.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private static class Ticket {
        /**
         * 票数
         */
        public volatile AtomicInteger tickets = new AtomicInteger(0);
    }
}
