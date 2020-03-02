package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 20:12
 * Description: active静态代理
 */
public class ActiveObjectProxy implements ActiveObject{

    private Servant servant;

    private SchedulerThread schedulerThread;

    public ActiveObjectProxy(Servant servant, SchedulerThread schedulerThread) {
        this.servant = servant;
        this.schedulerThread = schedulerThread;
    }

    /**
     * 组装新的字符串
     *
     * @param count
     * @param fillChar
     * @return
     */
    @Override
    public Result makeString(int count, char fillChar) {
        FutureResult futureResult = new FutureResult();
        this.schedulerThread.invoke(new MakeStringRequest(servant,futureResult,count,fillChar));
        return futureResult;
    }

    /**
     * 显示文本消息
     *
     * @param text
     */
    @Override
    public void displayString(String text) {
        this.schedulerThread.invoke(new DisplayStringRequest(servant,null,text));
    }
}
