package com.lonely.多线程基础.守护线程;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/12 19:41
 * Description: 设置守护线程
 */
public class DaemonThread {


    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(MessageFormat.format("{0}打印：{1}", Thread.currentThread().getName(),i));
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        //主线程操作
        TimeUnit.SECONDS.sleep(20);
        for (int i = 0; i < 100; i++) {
            System.out.println("main输出：" + i);
        }
        System.out.println("主线程结束");
    }

}
