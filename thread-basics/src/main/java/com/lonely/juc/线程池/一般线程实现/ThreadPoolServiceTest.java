package com.lonely.juc.线程池.一般线程实现;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * @auther: 15072
 * @date: 2020/3/6 14:12
 * Description: 底层的ThreadPoolService使用,由测试结果中可以看出线程池的大致运行原理
 * 1. submit一个任务的时候，先判断当前的任务工作者是否大于核心线程数，如果没有达到，则新创建一个工作者来执行该任务
 * 2. 如何已经达到了核心线程数，则先加入到等待队列中，当之前的任务工作者执行完了当前的任务后，会再从队里中提取一个来执行
 * 3. 如果此时队列已经满了，则在判断是否大于最大线程数，如果不大于最大线程数，则在开辟线程来处理新任务
 * 4. 随时判断线程空闲时间是否超过了设置的keepalive，如果超过了，则回收
 */
public class ThreadPoolServiceTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        //使用原生的ThreadPool
        //useThreadPool();

        //使用固定长度的线程池
        //useFixedThreadPool();

        //测试 future模式的返回值写法
        //useFutureGet();

        //测试 future模式的超时模式写法
        //useFutureGetTimeOut();

        //测试 future模式的取消任务写法
        //useFutureCancel();


        usedInvokeAll();


    }

    /**
     * 批量执行任务，但是存在弊端：由于不知道哪个任务先完成，假设有两个任务，一个任务需要10秒，一个任务需要20秒，那么循环取数据的时候，
     * 可能先获取的是20s的那个，那么10s的必然已经先执行完毕了，这样就浪费了时间，那么有没有办法实现：先返回的先处理呢。或者说处理完一个输出一个
     * @throws InterruptedException
     */
    private static void usedInvokeAll() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Callable<Integer>> tasks = new ArrayList<>();
        IntStream.range(0, 5).boxed().forEach(i ->
                tasks.add(() -> {
                    System.out.println(MessageFormat.format("线程：{0} start。。。输出：{1}",
                            Thread.currentThread().getName(), i));
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(MessageFormat.format("线程：{0} end。。。输出：{1}",
                            Thread.currentThread().getName(), i));
                    return i;
                })
        );
        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        futures.stream().map(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(System.out::println);
    }


    /**
     * 执行中的线程如何取消
     */
    private static void useFutureCancel() throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        //用于配合取消的第二种场景，重写线程工厂
        ExecutorService executorService2 = Executors.newFixedThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });


        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        Future<Integer> integerFuture = executorService.submit(() -> {
            System.out.println("thread start...");
            //这里不能使用 sleep，因为cancel()会打断其sleep
            /*try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            /*while (atomicBoolean.get()){

            }*/


            //两种打断执行方式

            //第一种，使用 cancel()和Thread.interrupted()配合  优雅的打断
            /*while (!Thread.interrupted()){
                System.out.println("执行中");
            }*/

            //第二种，使用重写线程池的线程工厂，创建线程的时候设置为守护线程
            while (atomicBoolean.get()) {
                //由于是守护线程，
            }

            System.out.println("over");
            return 1;
        });

        TimeUnit.MILLISECONDS.sleep(10);
        boolean cancel = integerFuture.cancel(true);
        System.out.println("cancel result:" + cancel);
        System.out.println("isCanceld: " + integerFuture.isCancelled());

        //如果线程正常结束，或者出现异常，或者已经cancel了则返回true
        System.out.println("isDwon: " + integerFuture.isDone());

        //中断后，拿不到返回值，会提示 CancellationException异常
        System.out.println("result: " + integerFuture.get());
    }


    /**
     * 使用 Future.get()方法会阻塞住  注意这里的中断，不会打断运行中的线程
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void useFutureGet() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> submit = executorService.submit(() -> {
            System.out.println("thread start.....");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "down";
        });

        Thread currentThread = Thread.currentThread();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentThread.interrupt();
        }).start();
        //TimeUnit.SECONDS.sleep(1);
        //Thread.currentThread().interrupt();
        String result = submit.get();
        System.out.println("result:" + result);
    }

    /**
     * 超时自动放弃结果,但是运行中的线程任然在继续执行
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    private static void useFutureGetTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> submit = executorService.submit(() -> {
            System.out.println("thread start.....");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "down";
        });

        String result = submit.get(3, TimeUnit.SECONDS);
        System.out.println("result:" + result);
    }


    /**
     * 使用固定核心线程数和最大线程数的场景
     */
    private static void useFixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new SimpleThread());
        executorService.submit(new SimpleThread());
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("线程池关闭");
    }


    /**
     * 使用原生threadpool
     */
    private static void useThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 60, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1), Thread::new, new ThreadPoolExecutor.AbortPolicy());

        threadPoolExecutor.submit(new SimpleThread());
        threadPoolExecutor.submit(new SimpleThread());
        threadPoolExecutor.submit(new SimpleThread());
        threadPoolExecutor.submit(new SimpleThread());
    }

    static class SimpleThread extends Thread {
        @Override
        public void run() {
            System.out.println(MessageFormat.format("线程：{0}执行了", Thread.currentThread().getName()));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
