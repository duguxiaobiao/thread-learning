package com.lonely.多线程基础.创建多线程的5种方式;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/12 12:44
 * Description:创建多线程之---基于线程池实现
 */
public class CreateThreadByThreadPool {

    public static void main(String[] args) throws InterruptedException {

        //创建容量为1的固定长度线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        //提交一个任务给线程池运行,这里使用匿名内部类创建线程
        executorService.submit(() -> {
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

        //关闭线程池,该方式是通知线程池要关闭了，但是会等待线程池中的任务完成，所以不算优雅的关闭
        executorService.shutdown();

        //优雅的关闭
        /*do {
            executorService.shutdown();
        } while (!executorService.isTerminated());*/


        //主线程睡眠2s
        TimeUnit.SECONDS.sleep(2);

        for (int i = 0; i < 10; i++) {
            System.out.println(MessageFormat.format("{0}输出:{1}", Thread.currentThread().getName(), i));
        }


    }

}
