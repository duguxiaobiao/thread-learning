package com.lonely.多线程基础.线程池.手写线程池实现;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/17 19:32
 * Description:
 */
public class Version2Test {

    public static void main(String[] args) {

        CustomerThreadPool2 threadPool = new CustomerThreadPool2(5,20);

        for (int i = 0; i < 40; i++) {
            int curr = i;
            threadPool.submit(()->{
                System.out.println(MessageFormat.format("{0}开始执行任务----{1}", Thread.currentThread().getName(),curr));
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println(MessageFormat.format("{0}中断了----{1}", Thread.currentThread().getName(),curr));
                    e.printStackTrace();
                }
                System.out.println(MessageFormat.format("{0}任务执行结束----{1}", Thread.currentThread().getName(),curr));
            });
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        threadPool.shutDown();


    }

}
