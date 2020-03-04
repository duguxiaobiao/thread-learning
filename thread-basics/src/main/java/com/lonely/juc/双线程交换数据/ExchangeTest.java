package com.lonely.juc.双线程交换数据;

import java.text.MessageFormat;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @auther: 15072
 * @date: 2020/3/2 20:09
 * Description: 用于两个线程之间交换数据，例如，两个人相约在某个时间地点来交易物品，只限于两个线程，且都到位了才可以交易，也就是
 * 两个线程都准备好了，才可以进行交换数据
 */
public class ExchangeTest {

    public static void main(String[] args) {

        Exchanger<String> exchanger = new Exchanger<>();

        //t1拿光碟去卖钱
        Thread t1 = new Thread(() -> {
            System.out.println(MessageFormat.format("线程：{0}正在向交易地点进军", Thread.currentThread().getName()));
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("线程：{0}正在已到达交易地点", Thread.currentThread().getName()));
            try {
                //使用这种方式，那么当前线程会因为超时中断，而另一个线程则会一直等待下去，知道手动中断
                exchanger.exchange("光碟", 1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println(MessageFormat.format("线程：{0},都到点了，不等了", Thread.currentThread().getName()));
                e.printStackTrace();
            }
            /*try {
                String result = exchanger.exchange("光碟");
                System.out.println(MessageFormat.format("线程：{0}已经交易成功，用光碟换到了{1}", Thread.currentThread().getName(), result));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }, "小明");


        Thread t2 = new Thread(() -> {
            System.out.println(MessageFormat.format("线程：{0}正在向交易地点进军", Thread.currentThread().getName()));
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("线程：{0}正在已到达交易地点", Thread.currentThread().getName()));
            try {
                String result = exchanger.exchange("钱");
                System.out.println(MessageFormat.format("线程：{0}已经交易成功，用钱换到了{1}", Thread.currentThread().getName(), result));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "小花");

        t1.start();
        t2.start();
    }

}
