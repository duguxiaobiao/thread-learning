package com.lonely.多线程基础.捕捉线程异常.捕捉异常的几种方式;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/16 16:00
 * Description: 基于线程池来捕捉异常
 */
public class CatchErrorByThreadPool {

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        try {
            //threadPool.execute(new SimpleThread());

            Future<?> submit = threadPool.submit(new SimpleThread());
            submit.get();
        } catch (Exception e) {
            System.out.println("拦截到异常");
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }

    /**
     * 随便定义的异常runnable
     */
    private static class SimpleThread implements Runnable {

        @Override
        public void run() {
            int i = 0;
            while (true) {
                i++;
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
                if (i > 20) {
                    throw new RuntimeException("线程自动抛出异常");
                }
            }
        }
    }


}
