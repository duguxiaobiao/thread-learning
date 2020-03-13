package com.lonely.juc.线程池.一般线程实现;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @auther: 15072
 * @date: 2020/3/9 13:39
 * Description: CompletableService的非组合操作api使用
 */
public class CompletableFutureNoBothUsedTest {

    private static Random random = new Random(System.currentTimeMillis());


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //使用 whenCompleAsync();
        //usedWhenCompleAsync();

        //使用 thenRun()
        //usedThenRunAsync();

        //测试 thenAccept()等 消费式接口
        //usedThenAccept();

        //测试 thenApply()方法的使用，允许消费前一步的结果，返回另一个结果，遇到异常了，会直接输出异常
        //usedThenApply();

        //使用handle()方法，功能跟 thenApply()类似，只是针对异常的时候不同处理模式
        usedHandle();

    }


    /**
     * 使用handle()方法，功能跟 thenApply()类似，只是针对异常的时候不同处理模式
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void usedHandle() throws InterruptedException, ExecutionException {
        //该方法和 thenApply()方法很相似，区别就是该方法支持拦截异常，然后在handler中处理异常消息
        CompletableFuture<String> compleFuture = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler).handle((r, t) -> {
            System.out.println("执行thenApply");
            randomSleep();
            return t != null ? "异常消息：" + t.getMessage() : "thenApply:" + r;
        });
        System.out.println(compleFuture.get());
    }


    /**
     * 测试 thenApply()方法的使用，允许消费前一步的结果，返回另一个结果
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void usedThenApply() throws InterruptedException, ExecutionException {
        //thenApply()方法入参是一个Funtion，前一步的返回值作为参数1，然后可以自定义返回另一个参数，可以用来操作前一步数据，返回另一个数据
        /*CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler).thenApply(r -> {
            System.out.println("执行thenApply");
            return "thenApply:" + r;
        });
        System.out.println(completableFuture.get());*/


        //使用 thenApplyAsync()不再过多介绍
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler).thenApplyAsync(r -> {
            System.out.println("执行thenApply");
            randomSleep();
            return "thenApply:" + r;
        });

        System.out.println(completableFuture.get());
        Thread.currentThread().join();
    }


    /**
     * 测试 thenAccept()等 消费式接口
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void usedThenAccept() throws InterruptedException, ExecutionException {
        //thenAccept(),消费级联的前一步的返回值,该方法属于消费类型的方法，无返回值
        /*CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler).thenAccept(r -> {
            System.out.println("结果是：" + r);
        });*/


        //同样的，也有异步的方法
        /*CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler).thenAcceptAsync(r -> {
            randomSleep();
            System.out.println("结果是：" + r);
        });*/


        //使用这种不适用级联的方式，就可以获取前面操作的返回值了
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler);
        future.thenAcceptAsync(r -> {
            randomSleep();
            System.out.println("结果是：" + r);
        });
        System.out.println(future.get());

        Thread.currentThread().join();
    }


    /**
     * thenRun()方法的使用，与whenComple()不同的是，thenRun()会放弃级联的前一步操作的返回值，返回void
     */
    private static void usedThenRunAsync() {
        //使用thenRun() 会在前一步执行完后，同步运行一个Runnable函数，但是与whenComple()不同的是，使用thenRun()方法后会丢弃前一步的返回值
        //CompletableFuture<Void> future = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler).thenRun(() -> System.out.println("执行结束"));
        //因此这里的get()返回的是null
        //System.out.println(future.get());

        //使用thenRunAsync()异步执行一个Runnable函数
        /*CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler).thenRunAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thenRunAsync()  over....");
        });
        Thread.currentThread().join();*/

        CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler, Executors.newSingleThreadExecutor()).thenRunAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thenRunAsync()  over....");
        }, Executors.newSingleThreadExecutor());
    }


    /**
     * whenCompleAsync()方法的使用，无返回值（虽然返回了CompletableFuture<Void>）是为了用于状态的监控和判断
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void usedWhenCompleAsync() throws InterruptedException, ExecutionException {

        //执行完毕后，同步执行回调方法
        /*CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler)
            .whenComplete((r, t) -> System.out.println("执行结束"));*/

        //执行完毕后，异步执行回调方法，注意如果真的想要异步执行，不能使用级联的方式，否则get()方法还是会等待whenCompleAsync()方法执行完后才获取到返回值
        /*CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler);
        completableFuture.whenCompleteAsync((r, t) -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行结束");
        });*/

        //执行完毕后，异步执行回调方法,但是这种写法，相对于get()方法来说并不能体现出来，因为在级联的写法情况下，get()还是会等待whenCompleAsync()方法执行完毕
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(CompletableFutureNoBothUsedTest::getSuppiler)
                .whenCompleteAsync((r, t) -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("执行结束");
                });
        System.out.println("执行结果：" + completableFuture.get());
        Thread.currentThread().join();
    }


    private static Integer getSuppiler() {
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
        return random.nextInt(1000);
    }

    /**
     * 随即休眠
     */
    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
