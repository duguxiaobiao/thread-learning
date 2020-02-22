package com.lonely.设计模式在多线程中的体现.观察者模式_监听线程状态;


/**
 * 定义监听器接口，实现了该接口，就表明支持监听
 */
public interface ListenerMonitor {


    /**
     * 监听方法
     * @param resource
     */
    void onEvent(ListenerResource resource);

}
