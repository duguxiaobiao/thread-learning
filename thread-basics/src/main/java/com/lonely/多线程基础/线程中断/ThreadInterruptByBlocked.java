package com.lonely.多线程基础.线程中断;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/13 15:04
 * Description: 基于 阻塞状态的线程中断场景
 */
public class ThreadInterruptByBlocked {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new BlockedThread());
        Thread t2 = new Thread(new BlockedThread());

        t1.start();

        TimeUnit.MILLISECONDS.sleep(500);
        t2.start();

        System.out.println("t1状态----" + t1.getState());
        System.out.println("t2状态----" + t2.getState());

        //中断t2
        t2.interrupt();

        System.out.println(MessageFormat.format("t1[STATE:{0}]----,[interrupt:{1}]",t1.getState(),t1.isInterrupted()) );
        System.out.println(MessageFormat.format("t2[STATE:{0}]----,[interrupt:{1}]",t2.getState(),t2.isInterrupted()) );

        System.out.println("主线程结束");
    }



    private static class BlockedThread implements Runnable{

        @Override
        public void run() {
            doing();
        }


        public static synchronized void doing(){
            while (true){
                //System.out.println(MessageFormat.format("{0}执行中.....", Thread.currentThread().getName()));
            }
        }
    }

}
