package com.lonely.juc.forkJoin框架;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * @auther: 15072
 * @date: 2020/3/5 13:27
 * Description: 基于带返回值的ForkJoinTask的使用
 */
public class RecursiveTaskUseTest {

    /**
     * 阀值，当超过阀值的时候，进行分割，没超过，即可以直接处理掉
     */
    private static final int threshold = 3;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        ForkJoinTask<Integer> submit = forkJoinPool.submit(new CustomerRecursiveTask(1, 100));
        Integer result = submit.get();
        System.out.println("result:" + result);
    }


    static class CustomerRecursiveTask extends RecursiveTask<java.lang.Integer> {

        private int start;

        private int end;

        CustomerRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected java.lang.Integer compute() {

            if (start > end) {
                throw new IllegalArgumentException("start > end error");
            }

            if ((end - start) <= threshold) {
                //没超过阀值,当前线程处理掉
                return IntStream.rangeClosed(start, end).sum();
            } else {
                //超过了阀值，进行切割
                int median = (end + start) / 2;
                CustomerRecursiveTask start = new CustomerRecursiveTask(this.start, median);
                CustomerRecursiveTask end = new CustomerRecursiveTask(median + 1, this.end);
                start.fork();
                end.fork();
                return start.join() + end.join();
            }
        }
    }
}
