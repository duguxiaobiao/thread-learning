package com.lonely.多线程基础.join;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/12 20:40
 * Description: join的使用
 */
public class JoinThread {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(MessageFormat.format("{0}输出:{1}", Thread.currentThread().getName(), i));
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        //不设置等待超时时间，即一直等待子线程执行完毕
        thread.join();

        //设置等待时间，10s
        //thread.join(10_000);

        for (int i = 0; i < 100; i++) {
            System.out.println("主线程输出：" + i);
        }
    }
}
