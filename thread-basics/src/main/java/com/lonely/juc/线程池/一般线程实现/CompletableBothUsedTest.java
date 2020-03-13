package com.lonely.juc.线程池.一般线程实现;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/3/9 17:03
 * Description: CompletableFuture 组合方式使用测试
 */
public class CompletableBothUsedTest {


    private static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {

        //同时执行两个任务，且以两个任务的返回值做统一处理，无返回值
        //usedThenAcceptBoth();

        //同时执行两个任务，都执行完毕后调用其runnable接口，不需要返回值
        //usedRunAfterBoth();

        //同时执行两个任务，且以两个任务的返回值做统一处理，且可以返回自定义的值，这是与 thenAcceptBoth的区别
        //usedThenCombine();

        //同时执行两个任务(要求两个方法的返回值一致)，哪个先执行完毕，就将执行完毕的返回值用于后续consumer函数的入参，该方法无返回值
        //usedAcceptEither();

        //同时执行两个任务(要求两个方法的返回值一致)，哪个先执行完比，将执行完毕的返回值用于后续consumer函数的入参，与acceptEither()方法不同是 applyToEither可以自定义返回值
        //usedApplyToEither();

        //同时执行两个任务，不需要要求返回值一致，因为该方法不用到返回值用作操作，当其中有一个执行完毕后，调用后续的runnable接口
        //usedRunAfterEither();

        //先后执行两个任务，将第一个任务的返回值可以用于第二个任务(提供了，用不用看用户如何使用),返回自定义返回值
        //usedThenCompose();

    }


    /**
     * 同时执行两个任务，都执行完毕后调用其runnable接口，不需要返回值
     *
     * @throws InterruptedException
     */
    private static void usedRunAfterBoth() throws InterruptedException {
        //同时执行两个任务，都执行完毕后调用其runnable接口，不需要返回值
        CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler)
                .runAfterBoth(CompletableFuture.supplyAsync(CompletableBothUsedTest::getStringSuppiler), () -> {
                    System.out.println("执行结束");
                });
        Thread.currentThread().join();
    }


    /**
     * 先后执行两个任务，将第一个任务的返回值可以用于第二个任务(提供了，用不用看用户如何使用),返回自定义返回值
     *
     * @throws InterruptedException
     */
    private static void usedThenCompose() throws InterruptedException {
        //先后执行两个任务，将第一个任务的返回值可以用于第二个任务(提供了，用不用看用户如何使用),返回自定义返回值
        CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler)
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("thenCompose thread start");
                    randomSleep();
                    System.out.println("thenCompose thread end");
                    return "结果:" + i;
                })).whenComplete((r, t) -> System.out.println("最终结果： " + r));
        Thread.currentThread().join();
    }

    /**
     * 同时执行两个任务，不需要要求返回值一致，因为该方法不用到返回值用作操作，当其中有一个执行完毕后，调用后续的runnable接口
     *
     * @throws InterruptedException
     */
    private static void usedRunAfterEither() throws InterruptedException {
        //同时执行两个任务，不需要要求返回值一致，因为该方法不用到返回值用作操作，当其中有一个执行完毕后，调用后续的runnable接口
        CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler)
                .runAfterEither(CompletableFuture.supplyAsync(CompletableBothUsedTest::getStringSuppiler), () -> {
                    System.out.println("执行完毕");
                });
        Thread.currentThread().join();
    }

    /**
     * 同时执行两个任务(要求两个方法的返回值一致)，哪个先执行完比，将执行完毕的返回值用于后续consumer函数的入参，
     * 与acceptEither()方法不同是 applyToEither可以自定义返回值
     *
     * @throws InterruptedException
     */
    private static void usedApplyToEither() throws InterruptedException {
        //同时执行两个任务(要求两个方法的返回值一致)，哪个先执行完比，将执行完毕的返回值用于后续consumer函数的入参，
        //与acceptEither()方法不同是 applyToEither可以自定义返回值
        CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler)
                .applyToEither(CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler), r -> {
                    return "结果：" + r;
                }).whenComplete((r, t) -> System.out.println("最终结果：" + r));
        Thread.currentThread().join();
    }


    /**
     * 同时执行两个任务，哪个先执行完毕，就将执行完毕的返回值用于后续consumer函数的入参，该方法无返回值
     *
     * @throws InterruptedException
     */
    private static void usedAcceptEither() throws InterruptedException {
        CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler)
                .acceptEither(CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler), r -> {
                    System.out.println("最终结果：" + r);
                });
        Thread.currentThread().join();
    }

    /**
     * 同时执行两个任务，且以两个任务的返回值做统一处理，且可以返回自定义的值，这是与 thenAcceptBoth的区别
     *
     * @throws InterruptedException
     */
    private static void usedThenCombine() throws InterruptedException {
        CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler)
                .thenCombine(CompletableFuture.supplyAsync(CompletableBothUsedTest::getStringSuppiler), (i, s) -> {
                    return s.length() > i;
                }).whenComplete((r, t) -> System.out.println("最终结果：" + r));
        Thread.currentThread().join();
    }


    /**
     * 同时执行两个任务，且以两个任务的返回值做统一处理，无返回值
     *
     * @throws InterruptedException
     */
    private static void usedThenAcceptBoth() throws InterruptedException {
        CompletableFuture.supplyAsync(CompletableBothUsedTest::getIntegerSuppiler)
                .thenAcceptBoth(CompletableFuture.supplyAsync(CompletableBothUsedTest::getStringSuppiler), (i, s) -> {
                    System.out.println("最终输出结果：" + (s.length() > i));
                });
        Thread.currentThread().join();
    }


    /**
     * 模拟返回Integer类型的 Suppiler()
     *
     * @return
     */
    private static Integer getIntegerSuppiler() {
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
     * 模拟返回String类型的 Suppiler()
     *
     * @return
     */
    private static String getStringSuppiler() {
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
        return "Random" + random.nextInt(1000);
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
