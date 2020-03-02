package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 19:30
 * Description:
 */
public interface ActiveObject {

    /**
     * 组装新的字符串
     *
     * @param count
     * @param fillChar
     * @return
     */
    Result makeString(int count, char fillChar);

    /**
     * 显示文本消息
     *
     * @param text
     */
    void displayString(String text);

}
