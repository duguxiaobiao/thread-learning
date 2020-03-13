package com.lonely.juc.线程池.一般线程实现;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @auther: 15072
 * @date: 2020/3/8 17:29
 * Description: 使用CompletionService的方式可以解决Future模式的弊端---即无法将结果根据哪个先执行完，先处理那个的问题
 */
public class CompletionServiceTest {


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //CompletionService需要一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        //定义一个接受返回值的queue
        BlockingQueue<Future<Integer>> blockingQueue = new LinkedBlockingQueue<>();

        //通过指定的线程池来执行任务，将返回的future放入到指定的容器中
        CompletionService completionService = new ExecutorCompletionService(executorService, blockingQueue);
        IntStream.range(0, 5).boxed().map(CallableThread::new).forEach(completionService::submit);

        Future<Integer> currFuture = null;
        while ((currFuture = blockingQueue.take()) != null) {
            System.out.println(currFuture.get());
        }

    }


    static class CallableThread implements Callable<Integer> {

        private int num;

        CallableThread(int num) {
            this.num = num;
        }

        @Override
        public Integer call() throws Exception {
            //随机休眠
            int time = ThreadLocalRandom.current().nextInt(5);
            System.out.println(MessageFormat.format("线程：{0} start .. -----[{1}----{2}]",
                    Thread.currentThread().getName(), num, time));

            TimeUnit.SECONDS.sleep(time + 1);
            System.out.println(MessageFormat.format("线程：{0} end .. -----[{1}---{2}]",
                    Thread.currentThread().getName(), num, time));
            return num;
        }
    }


}
