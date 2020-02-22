package com.lonely.多线程基础.创建多线程的5种方式;

import java.text.MessageFormat;
import java.util.concurrent.*;

/**
 * @auther: 15072
 * @date: 2020/2/12 12:26
 * Description:创建多线程之---基于带返回值的异步Callable接口实现
 */
public class CreateThreadByFutureTaskAndCallable {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //基于实现Callable类的方式 Future是一个接口，使用FutureTask实现类处理
        FutureTask<Integer> future = new FutureTask<>(new ImplementCallable());

        //在使用Thread类启动，注意 Future任然是一个Runnable接口，所以还是需要Thread类启动或者使用线程池启动
        Thread thread = new Thread(future);
        thread.start();

        Integer sum = future.get();
        System.out.println(MessageFormat.format("实现Callable的线程计算的结果：{0}", sum));

        Integer mainSum = 0;
        for (int i = 0; i < 10; i++) {
            mainSum += i;
            System.out.println(MessageFormat.format("{0}输出:{1}", Thread.currentThread().getName(), i));
        }
        System.out.println(MessageFormat.format("Main线程计算的结果：{0}", mainSum));
    }


    private static class ImplementCallable implements Callable<Integer> {
        @Override
        public Integer call() {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
                System.out.println(MessageFormat.format("{0}输出：{1}", Thread.currentThread().getName(), i));
                try {
                    //睡眠500毫秒
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return sum;
        }
    }

}
