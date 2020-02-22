package com.lonely.多线程基础.捕捉线程异常.捕捉异常的几种方式;

import java.text.MessageFormat;
import java.util.concurrent.ThreadFactory;

/**
 * @auther: 15072
 * @date: 2020/2/16 15:54
 * Description: 通过线程工厂，创建线程的时候设置setUncaughtExceptionHandler(),避免每次手动指定
 */
public class CatchErrorByThreadFactory {

    public static void main(String[] args) {

        CustomerThreadFactory factory = new CustomerThreadFactory();

        factory.newThread(() -> {
            int i = 0;
            while (true) {
                i++;
                if (i > 20) {
                    throw new RuntimeException("线程中手动抛出异常");
                }
            }
        }).start();


    }


    /**
     * 自定义线程工厂
     */
    private static class CustomerThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setUncaughtExceptionHandler((errorThread, e) -> {
                System.out.println(MessageFormat.format("{0}出现异常，异常原因：{1}", errorThread.getName(), e.getMessage()));
            });
            return thread;
        }
    }
}
