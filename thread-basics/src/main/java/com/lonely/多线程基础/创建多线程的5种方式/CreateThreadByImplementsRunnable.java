package com.lonely.多线程基础.创建多线程的5种方式;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/12 12:11
 * Description:创建多线程之---基于 extends thread方式
 * Thread类的构造方法中有一种传入Runnable接口的构造方法，这里就通过Runnable来创建Thread对象
 */
public class CreateThreadByImplementsRunnable {

    public static void main(String[] args) throws InterruptedException {

        //基于继承Thread类的方式，使用start方法启动线程
        Thread thread = new Thread(new ImplementsRunnable());
        thread.start();

        //主线程睡眠2s
        TimeUnit.SECONDS.sleep(2);

        for (int i = 0; i < 10; i++) {
            System.out.println(MessageFormat.format("{0}输出:{1}", Thread.currentThread().getName(), i));
        }
    }

    /**
     * 实现 Runnable接口
     */
    private static class ImplementsRunnable implements Runnable {
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
    }
}
