package com.lonely.juc.线程池.一般线程实现;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @auther: 15072
 * @date: 2020/3/8 19:37
 * Description: CompletableFuture类的通过工厂方法创建使用,使用该类，可以实现基于Future模式的不足(多个线程的并发执行，根据先执行完先返回)的情况，
 * 例如使用 whenComplete()回调使用。
 */
public class CompletableFutureCreateTest {


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //实现Runnable线程，使用默认的线程池，可以执行完后回调一个接口，主动触发，而不需要调用者等待获取
        //usedRunnableDefaultExecuteImpl();

        //执行Runnable线程，使用自定义线程池服务来执行,提供执行完毕后的回调结果调用---whenComplete()
        //usedRunnableCustomerExecuteImpl();

        //使用默认线程池执行callable接口服务， 使用 supplyAsync()构造
        //usedCallableDefaultExecuteImpl();

        //使用指定的线程池来执行带返回值的callable任务
        //usedCallableCustomerExecuteImpl();

        //使用allOf方式来执行一批次任务，可以等都执行完毕后统一回调
        //usedAllOfDefaultExecuteImpl();

        usedAnyOfDefaultExecuteImpl();

    }

    /**
     * 使用anyOf()来执行一批任务，只要返回一个接口就行了，但是任务都会执行，一般是哪个任务先执行完毕后先返回
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void usedAnyOfDefaultExecuteImpl() throws InterruptedException, ExecutionException {
        CompletableFuture<Object> future = CompletableFuture.anyOf(CompletableFuture.supplyAsync(() -> {
            int sleepTime = ThreadLocalRandom.current().nextInt(5) + 1;
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}--sleepTime:{2}]",
                    Thread.currentThread().getName(), 1, sleepTime));
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}---sleepTime:{2}]",
                    Thread.currentThread().getName(), 1, sleepTime));
            return 1;
        }), CompletableFuture.supplyAsync(() -> {
            int sleepTime = ThreadLocalRandom.current().nextInt(5) + 1;
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}--sleepTime:{2}]",
                    Thread.currentThread().getName(), 2, sleepTime));
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}---sleepTime:{2}]",
                    Thread.currentThread().getName(), 1, sleepTime));
            return 2;
        }));

        System.out.println(future.get());
    }


    /**
     * 使用allOf方式来执行一批次任务，可以等都执行完毕后统一回调
     * 注意：1. 如果其中一个线程出现异常了，其余的任务不会受到影响
     * 2. 如果有多个异常，后面执行的任务的异常消息会覆盖前面任务的异常消息
     *
     * @throws InterruptedException
     */
    private static void usedAllOfDefaultExecuteImpl() throws InterruptedException {
        CompletableFuture.allOf(CompletableFuture.supplyAsync(() -> {
            int sleepTime = ThreadLocalRandom.current().nextInt(5) + 1;
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}--sleepTime:{2}]",
                    Thread.currentThread().getName(), 1, sleepTime));
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //在这里故意抛出一个异常，验证这种批量执行的情况的时候，如果其中一个线程出现异常了，其余的任务是否执行
            //结果是不会影响其余线程
            System.out.println(1 / 0);
            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}---sleepTime:{2}]",
                    Thread.currentThread().getName(), 1, sleepTime));
            return 1;
        }), CompletableFuture.supplyAsync(() -> {
            int sleepTime = ThreadLocalRandom.current().nextInt(5) + 1;
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}--sleepTime:{2}]",
                    Thread.currentThread().getName(), 2, sleepTime));
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //这里在第一个任务故意抛出异常的情况下，这里也抛出异常，验证如果有多个异常，在whenComple()方法中的异常对象t，会输出什么
            //验证结果是后面执行的任务的异常消息会覆盖前面任务的异常消息
            if (true) {
                throw new RuntimeException("故意抛出异常");
            }
            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}---sleepTime:{2}]",
                    Thread.currentThread().getName(), 1, sleepTime));
            return 2;
        })).whenComplete((r, t) -> System.out.println("都执行完毕了,异常消息：" + t.getMessage()));
        Thread.currentThread().join();
    }


    /**
     * 使用指定的线程池来执行带返回值的callable任务
     */
    private static void usedCallableCustomerExecuteImpl() {
        IntStream.range(0, 5).boxed().forEach(num -> CompletableFuture.supplyAsync(() -> {
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}]", Thread.currentThread().getName(), num));
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}]", Thread.currentThread().getName(), num));
            return num;
        }, Executors.newFixedThreadPool(5)).whenComplete((r, t) -> System.out.println("返回结果：" + r)));
    }

    /**
     * 使用默认线程池执行callable接口服务， 使用 supplyAsync()构造
     *
     * @throws InterruptedException
     */
    private static void usedCallableDefaultExecuteImpl() throws InterruptedException {
        IntStream.range(0, 5).boxed().forEach(num -> CompletableFuture.supplyAsync(() -> {
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}]", Thread.currentThread().getName(), num));
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}]", Thread.currentThread().getName(), num));
            return num;
        }).whenComplete((r, t) -> System.out.println("返回值：" + r)));
        Thread.currentThread().join();
    }


    /**
     * 执行Runnable线程，使用自定义线程池服务来执行,提供执行完毕后的回调结果调用---whenComplete()
     */
    private static void usedRunnableCustomerExecuteImpl() {
        //使用自己提供的线程池来执行线程， whenComplete()是等线程执行完毕后的的回调接口
        CompletableFuture.runAsync(new SimpleRunnableThread(1), Executors.newSingleThreadExecutor())
                .whenComplete((v, t) -> System.out.println("执行完毕"));
    }


    /**
     * 实现Runnable线程，使用默认的线程池
     *
     * @throws InterruptedException
     */
    private static void usedRunnableDefaultExecuteImpl() throws InterruptedException {
        //使用不提供线程池的实现，使用默认的线程池，以守护线程的方式执行线程，所以需要当前线程不能结束，这里使用join，保证线程正常执行
        CompletableFuture.runAsync(new SimpleRunnableThread(1)).whenComplete((v, t) -> System.out.println("执行完毕"));
        Thread.currentThread().join();
    }


    static class SimpleCallable implements Callable<Integer> {
        private int num;

        SimpleCallable(int num) {
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}]", Thread.currentThread().getName(), num));
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5) + 1);
            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}]", Thread.currentThread().getName(), num));
            return num;
        }
    }


    static class SimpleRunnableThread extends Thread {

        private int num;

        SimpleRunnableThread(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            System.out.println(MessageFormat.format("线程：{0} start.....[num:{1}]", Thread.currentThread().getName(), num));

            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(MessageFormat.format("线程：{0} end.....[num:{1}]", Thread.currentThread().getName(), num));
        }
    }
}
