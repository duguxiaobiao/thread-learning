package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * 将每一个实例方法抽象成命令模式对象
 */
public abstract class MethodRequest {

    /**
     * 具体代码执行者
     */
    protected final Servant servant;

    /**
     * 返回的异步结果
     */
    protected final FutureResult futureResult;

    protected MethodRequest(Servant servant, FutureResult futureResult) {
        this.servant = servant;
        this.futureResult = futureResult;
    }


    /**
     * 执行命令
     */
    public abstract void execute();

}
