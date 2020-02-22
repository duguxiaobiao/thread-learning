package com.lonely.多线程基础.线程池.手写线程池实现;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/17 13:02
 * Description: 版本1测试
 */
public class Version1Test {


    public static void main(String[] args) {

        CustomerThreadPool customerThreadPool = new CustomerThreadPool();

        for (int i = 0; i < 40; i++) {
            int curr = i;
            customerThreadPool.submit(()->{
                System.out.println(MessageFormat.format("i:{0} ,thread:{1}执行任务",curr, Thread.currentThread().getName()));
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(MessageFormat.format("i:{0} 执行任务完毕",curr));
            });
        }


    }

}
