package com.lonely.设计模式在多线程中的体现.Future模式;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/22 21:26
 * Description: 具体如何获取数据的过程
 */
@FunctionalInterface
public interface FutureTask<T> {

    /**
     * 执行过程
     *
     * @return
     */
    T call();

}
