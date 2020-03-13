package com.lonely.juc.forkJoin框架;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @auther: 15072
 * @date: 2020/3/5 13:52
 * Description: 基于无返回值的 ForkJoinTask的使用
 */
public class RecursiveActionUseTest {

    /**
     * 阀值，当超过阀值的时候，进行分割，没超过，即可以直接处理掉
     */
    private static final int threshold = 3;

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(new CustomerRecursiveAction(1, 100));

        forkJoinPool.shutdown();
        while (!forkJoinPool.isTerminated()){
        }
        System.out.println(atomicInteger.get());
    }


    static class CustomerRecursiveAction extends RecursiveAction {

        private int start;

        private int end;

        CustomerRecursiveAction(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (start > end) {
                throw new IllegalArgumentException("start>end error");
            }

            if ((end - start) <= threshold) {
                //直接计算
                atomicInteger.addAndGet(IntStream.rangeClosed(start, end).sum());
            } else {
                //分割
                int median = (start + end) / 2;
                CustomerRecursiveAction start = new CustomerRecursiveAction(this.start, median);
                CustomerRecursiveAction end = new CustomerRecursiveAction(median + 1, this.end);
                start.fork();
                end.fork();
            }
        }
    }


}
