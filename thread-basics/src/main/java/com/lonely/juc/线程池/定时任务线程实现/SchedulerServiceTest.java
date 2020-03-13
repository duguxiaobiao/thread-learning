package com.lonely.juc.线程池.定时任务线程实现;

import java.text.MessageFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/3/6 15:11
 * Description:
 */
public class SchedulerServiceTest {

    public static void main(String[] args) {

        System.out.println("currtime:" + System.currentTimeMillis());

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

        //指定延时后执行程序
        //scheduledExecutorService.schedule(new SimpleThread(), 1, TimeUnit.SECONDS);

        //指定延时后执行程序，然后间隔指定时长重复执行,
        // 注意：如果执行的任务时间超过了间隔，则会等任务执行完后，直接运行
        //如果执行的任务时间没有超过间隔，则等到间隔时间到了执行
        //例如本例中，如果程序执行时间没有超过，则等10秒后执行，如果执行时间要15秒，则执行完后立即执行下一批次
        //scheduledExecutorService.scheduleAtFixedRate(new SimpleThread(),1,10,TimeUnit.SECONDS);

        //指定延时后执行程序，然后等程序执行完后延时指定时间后执行下一批次，跟 scheduleAtFixedRate()有区别
        //例如本例中，如果不管程序执行时间是否超过了间隔时间，都以执行时间+间隔时间作为下一次批次执行时间
        scheduledExecutorService.scheduleWithFixedDelay(new SimpleThread(),1,10,TimeUnit.SECONDS);

    }


    static class SimpleThread extends Thread {
        @Override
        public void run() {
            System.out.println(MessageFormat.format("线程：{0}执行了:{1}", Thread.currentThread().getName(), System.currentTimeMillis()));
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
