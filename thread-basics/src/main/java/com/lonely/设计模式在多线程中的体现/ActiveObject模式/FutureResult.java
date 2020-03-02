package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 19:34
 * Description: 异步result
 */
public class FutureResult implements Result {

    /**
     * 是否拿到结果
     */
    private boolean flag = false;

    /**
     * 最终的结果
     */
    private Object result;

    public void setResult(Object result) {
        synchronized (this) {
            this.flag = true;
            this.result = result;
            this.notifyAll();
        }
    }

    /**
     * 获取结果
     *
     * @return
     */
    @Override
    public Object getResult() {
        synchronized (this) {
            while (!flag) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //拿到结果了，返回结果
            return this.result;
        }
    }
}
