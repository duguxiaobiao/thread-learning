package com.lonely.多线程基础.捕捉线程异常.捕捉异常的几种方式;

import java.text.MessageFormat;

/**
 * @auther: 15072
 * @date: 2020/2/16 15:42
 * Description: 通过设置Thread.setUncaughtExceptionHandler()来获取异常，进而进行处理
 */
public class CatchErrorByUncaughtExceptionHandler {

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            int i = 0;
            while (true) {
                i++;
                if (i > 20) {
                    throw new RuntimeException("线程中手动抛出异常");
                }
            }
        });
        t1.setUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println(MessageFormat.format("{0}出现了异常，异常原因：{1}", thread.getName(), throwable.getMessage()));
        });
        t1.start();
    }

}
