package com.lonely.设计模式在多线程中的体现.观察者模式_监听线程状态;

/**
 * 线程状态
 */
public enum ThreadState {

    /**
     * 运行中
     */
    RUNNING,

    /**
     * 关闭
     */
    DOWN,

    /**
     * 异常退出
     */
    ERROR;

}
