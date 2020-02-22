package com.lonely.设计模式在多线程中的体现.Future模式;

/**
 * @auther: 15072
 * @date: 2020/2/22 21:36
 * Description:
 */
public class FutureService<T> {


    /**
     * 提交任务
     *
     * @param task
     * @return
     */
    public Future<T> submit(FutureTask<T> task) {
        AsynFuture<T> future = new AsynFuture<>();
        new Thread(() -> {
            T result = task.call();
            future.notifyDown(result);
        }).start();
        return future;
    }

}
