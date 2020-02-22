package com.lonely.设计模式在多线程中的体现.Future模式;

/**
 * 凭据
 */
public interface Future<T> {

    /**
     * 获取数据接口
     *
     * @return
     * @throws InterruptedException
     */
    T get() throws InterruptedException;
}
