package com.lonely.设计模式在多线程中的体现.Future模式;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/22 21:43
 * Description:
 */
public class MainTest {

    public static void main(String[] args) throws InterruptedException {

        FutureService<String> futureService = new FutureService<>();
        Future<String> future = futureService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "执行完毕";
        });

        //先去执行其他事情
        TimeUnit.SECONDS.sleep(2);

        //来获取数据
        String s = future.get();
        System.out.println("结果：" + s);

    }


}
