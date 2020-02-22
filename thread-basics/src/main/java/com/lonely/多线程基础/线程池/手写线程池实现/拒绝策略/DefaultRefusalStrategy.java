package com.lonely.多线程基础.线程池.手写线程池实现.拒绝策略;

/**
 * @auther: 15072
 * @date: 2020/2/17 19:10
 * Description: 默认的拒绝策略----抛出异常
 */
public class DefaultRefusalStrategy implements RefusalStrategy {

    /**
     * 拒绝方法
     */
    @Override
    public void refuse() {
        throw new RuntimeException("线程池满了，拒绝加入");
    }
}
