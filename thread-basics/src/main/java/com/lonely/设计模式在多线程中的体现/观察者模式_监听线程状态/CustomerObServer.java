package com.lonely.设计模式在多线程中的体现.观察者模式_监听线程状态;

import java.text.MessageFormat;

/**
 * @auther: 15072
 * @date: 2020/2/20 17:45
 * Description: 自定义观察者,实现监听接口，表明该观察者具有监听效果
 */
public class CustomerObServer implements ListenerMonitor {

    /**
     * 锁资源
     */
    private static final Object OBJECT = new Object();

    /**
     * 监听方法，因为该观察者可能对应多个待观察的对象，存在并发的可能，加个锁
     *
     * @param resource
     */
    @Override
    public void onEvent(ListenerResource resource) {
        synchronized (OBJECT) {
            //输出结果
            System.out.println(MessageFormat.format("线程{0}当前状态：{1}，异常消息：{2}", resource.getThread().getName(),
                    resource.getThreadState(), resource.getThrowable()));
        }
    }
}
