package com.lonely.设计模式在多线程中的体现.CountDown模式;

/**
 * @auther: 15072
 * @date: 2020/2/24 16:22
 * Description: countdown实现
 */
public class CountDown {

    /**
     * 计数器最大数
     */
    private final int total;

    /**
     * 当前是使用到了第几个资格
     */
    private int index;


    public CountDown(int total) {
        this.total = total;
    }

    /**
     * 占用锁定一个资源
     */
    public void down() {
        synchronized (this) {
            this.index++;
            if (this.index > total) {
                throw new RuntimeException("没有多余资格");
            }
            if (this.index == total) {
                //刚好满了，则通知休息的线程开始执行下面的代码了
                this.notify();
            }
        }
    }

    /**
     * 等待全部准备好
     */
    public void await() throws InterruptedException {
        synchronized (this) {
            while (this.index != this.total) {
                //还没达到总数,休息先
                this.wait();
            }
        }
    }
}
