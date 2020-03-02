package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 19:39
 * Description: 获取真实结果
 */
public class RealResult implements Result {

    /**
     * 真实值
     */
    private final Object realObject;

    public RealResult(Object realObject) {
        this.realObject = realObject;
    }


    /**
     * 获取结果
     *
     * @return
     */
    @Override
    public Object getResult() {
        return this.realObject;
    }
}
