package com.lonely.设计模式在多线程中的体现.Future模式;

/**
 * @auther: 15072
 * @date: 2020/2/22 21:33
 * Description: 异步方式获取数据
 */
public class AsynFuture<T> implements Future<T> {

    /**
     * 获取数据的操作是否执行完毕，即是否已经返回数据了
     */
    private volatile boolean isDone;

    /**
     * 返回的数据
     */
    private T result;

    /**
     * 获取数据接口
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public T get() throws InterruptedException {
        synchronized (this) {
            while (!isDone) {
                this.wait();
            }
        }
        return this.result;
    }

    /**
     * 通知执行完毕了
     *
     * @param result
     */
    public void notifyDown(T result) {
        synchronized (this) {
            this.isDone = true;
            this.result = result;
            this.notifyAll();
        }

    }
}
