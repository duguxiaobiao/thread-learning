package com.lonely.设计模式在多线程中的体现.CountDown模式;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/24 16:27
 * Description:
 */
public class MainTest {

    public static void main(String[] args) throws InterruptedException {

        CountDown countDown = new CountDown(5);

        for (int i = 0; i < 5; i++) {
            int curr = i;
            new Thread(() -> {
                System.out.println(MessageFormat.format("执行：{0}线程---{1}", Thread.currentThread().getName(), curr));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(MessageFormat.format("{0}执行完毕", Thread.currentThread().getName()));
                countDown.down();
            }).start();
        }

        countDown.await();
        System.out.println("开始执行主线程了");

    }

}
