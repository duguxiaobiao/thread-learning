package com.lonely.多线程基础.创建多线程的5种方式;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/12 12:19
 * Description:创建多线程之---基于匿名内部类方式
 */
public class CreateThreadByAnonymousInnerClass {

    public static void main(String[] args) throws InterruptedException {

        //jdk1.7之前这样创建
        /*Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(MessageFormat.format("{0}输出：{1}", Thread.currentThread().getName(), i));
                    try {
                        //睡眠500毫秒
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };*/

        //jdk1.8之后使用 lambda表达式创建
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(MessageFormat.format("{0}输出：{1}", Thread.currentThread().getName(), i));
                try {
                    //睡眠500毫秒
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        //主线程睡眠2s
        TimeUnit.SECONDS.sleep(2);

        for (int i = 0; i < 10; i++) {
            System.out.println(MessageFormat.format("{0}输出:{1}", Thread.currentThread().getName(), i));
        }
    }
}
