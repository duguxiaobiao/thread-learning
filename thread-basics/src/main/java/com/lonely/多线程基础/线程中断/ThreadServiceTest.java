package com.lonely.多线程基础.线程中断;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/13 18:00
 * Description:
 */
public class ThreadServiceTest {

    public static void main(String[] args) throws InterruptedException {

        ThreadService threadService = new ThreadService();
        threadService.submit(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        });

        //TimeUnit.SECONDS.sleep(10);
        long startTime = System.currentTimeMillis();

        threadService.shutDown(15_000);

        long endTiem = System.currentTimeMillis();

        System.out.println("最终使用时间：" + (endTiem - startTime) + "ms");

    }

}
