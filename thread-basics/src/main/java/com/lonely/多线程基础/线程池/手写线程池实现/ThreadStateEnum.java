package com.lonely.多线程基础.线程池.手写线程池实现;

/**
 * 线程状态枚举
 */
public enum ThreadStateEnum {

    /**
     * 可使用
     */
    FREE,
    /**
     * 使用中
     */
    RUNNING,
    /**
     * 阻塞中
     */
    BLOCKED,
    /**
     * 关闭
     */
    CLOSE

}
