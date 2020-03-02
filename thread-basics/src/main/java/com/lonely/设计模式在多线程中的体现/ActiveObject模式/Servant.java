package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/25 19:33
 * Description: activeoObject代码的具体实现
 */
public class Servant implements ActiveObject {

    private Random random = new Random(System.currentTimeMillis());

    /**
     * 组装新的字符串
     *
     * @param count
     * @param fillChar
     * @return
     */
    @Override
    public Result makeString(int count, char fillChar) {
        char[] buffer = new char[count];
        //休眠一段时间，模拟操作
        Arrays.fill(buffer,fillChar);
        /*try {
            TimeUnit.MILLISECONDS.sleep(this.random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return new RealResult(new String(buffer));
    }

    /**
     * 显示文本消息
     *
     * @param text
     */
    @Override
    public void displayString(String text) {
        System.out.println(MessageFormat.format("{0}--------displeayString:{1}", Thread.currentThread().getName(), text));
        try {
            TimeUnit.MILLISECONDS.sleep(this.random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
