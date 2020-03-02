package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 20:22
 * Description: 通过工厂创建proxy，提供唯一的入口
 */
public class ActiveObjectFactory {

    private ActiveObjectFactory() {
    }


    /**
     * 创建activeobject代理
     *
     * @return
     */
    public static ActiveObjectProxy createActiveObject() {
        Servant servant = new Servant();
        ActiveObjectQueue activeObjectQueue = new ActiveObjectQueue();
        SchedulerThread schedulerThread = new SchedulerThread(activeObjectQueue);
        ActiveObjectProxy activeObjectProxy = new ActiveObjectProxy(servant, schedulerThread);
        schedulerThread.start();
        return activeObjectProxy;
    }


}
