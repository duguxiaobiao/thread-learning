package com.lonely.多线程基础.线程池.手写线程池实现.拒绝策略;


/**
 * 拒绝策略接口
 */
public interface RefusalStrategy {

    /**
     * 拒绝方法
     */
    void refuse();
}
