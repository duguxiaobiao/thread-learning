package com.lonely.juc.线程池.拒绝策略;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @auther: 15072
 * @date: 2020/3/7 12:14
 * Description: 线程池拒绝策略使用
 * jdk提供了四大拒绝策略，默认为  AbortPolicy(即直接抛出RejectedExecutionException异常)
 */
public class UsedRefusalStrategyTest {

    public static void main(String[] args) throws InterruptedException {
        //测试 AbortPolicy拒绝策略效果
        //usedAbortPolicy();

        //测试 CallerRunsPolicy拒绝策略效果
        //userdCallerRunsPolicy();

        //测试 DiscardPolicy拒绝策略效果
        //usedDiscardPolicy();

        //测试 DiscardOldestPolicy拒绝策略效果
        //usedDiscardOldestPolicy();

        //测试 自定义拒绝策略效果
        usedCustomerProlicy();


    }

    /**
     * 使用自定义拒绝策略
     */
    private static void usedCustomerProlicy() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), Executors.defaultThreadFactory(), new CustomerPolicy());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());
    }


    /**
     * 测试 DiscardOldestPolicy拒绝策略效果------将最早进入等待队列的线程剔除，将当前线程execute
     *
     * @throws InterruptedException
     */
    private static void usedDiscardOldestPolicy() throws InterruptedException {
        //这里故意将队列设置为2次，用来体现进入队列的线程的先后之别
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());

        threadPoolExecutor.submit(new CallableSimple(1));
        threadPoolExecutor.submit(new CallableSimple(2));
        //这里故意休息一下，提先num2先进入队列，num3后进入队列，这样就可以将先进入的老任务优先剔除
        TimeUnit.MILLISECONDS.sleep(10);
        threadPoolExecutor.submit(new CallableSimple(3));
        threadPoolExecutor.submit(new CallableSimple(4));

        //线程池满了后，该任务会触发拒绝策略，则会从等待队列中剔除最新进入的那个任务
        //因为在本例中，等待队列中有两个线程Num2，num3，且num2先进入，则会将num2剔除，运行num5
        threadPoolExecutor.submit(new CallableSimple(5));
        threadPoolExecutor.shutdown();
        while (!threadPoolExecutor.isTerminated()) {

        }
        System.out.println("over.............");
    }

    /**
     * 测试 DiscardPolicy拒绝策略效果 -------------直接放弃本次提交的任务
     */
    private static void usedDiscardPolicy() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());

        //可以从结果中看到，这种拒绝策略就是直接放弃当前提交的任务，直接放弃该任务
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.shutdown();
        while (!threadPoolExecutor.isTerminated()) {

        }
        System.out.println("over.......");
    }


    /**
     * 测试 CallerRunsPolicy拒绝策略效果----------------有调用者线程来直接同步运行run方法
     */
    private static void userdCallerRunsPolicy() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());

        //这种情况下，拒绝策略是直接有当前线程(这里是main线程)来直接执行同步方法，直接run()
        threadPoolExecutor.submit(new Simple());
    }


    /**
     * 使用AbortPolicy拒绝策略效果--------------直接抛出异常
     */
    private static void usedAbortPolicy() {
        //使用 AbortPolicy拒绝策略
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


        //根据上述的配置，最多只支持三个任务(因为在任务里故意阻塞)
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());
        threadPoolExecutor.submit(new Simple());

        //可以看出该任务被拒绝了，且是直接抛出异常
        threadPoolExecutor.submit(new Simple());
    }


    private static void call(Runnable runnable) {
        System.out.println(MessageFormat.format("当前线程:{0} 收到来自线程：{1}的回调", Thread.currentThread().getName()));
    }

    static class CustomerPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("当前线程池满了");
        }
    }

    static class CallableSimple implements Callable<Integer> {

        private int num;

        CallableSimple(int num) {
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(MessageFormat.format("thrad:{0}-------{1} start..", Thread.currentThread().getName(), num));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("thrad:{0}---------{1} end..", Thread.currentThread().getName(), num));
            return num;
        }
    }

    static class Simple extends Thread {
        @Override
        public void run() {
            System.out.println(MessageFormat.format("thread:{0} start....", Thread.currentThread().getName()));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("thread:{0} end....", Thread.currentThread().getName()));
        }
    }
}
