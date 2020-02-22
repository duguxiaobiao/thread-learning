package com.lonely.设计模式在多线程中的体现.观察者模式_监听线程状态;

/**
 * @auther: 15072
 * @date: 2020/2/20 17:46
 * Description: 自定义待观察的对象
 */
public class CustomerSubject extends Thread {

    /**
     * 监听对象
     */
    private ListenerMonitor listenerMonitor;

    public CustomerSubject(ListenerMonitor listenerMonitor) {
        this.listenerMonitor = listenerMonitor;
    }

    /**
     * 触发监听，通知对象
     *
     * @param resource
     */
    public void changeNotify(ListenerResource resource) {
        this.listenerMonitor.onEvent(resource);
    }

    @Override
    public void run() {

    }
}
