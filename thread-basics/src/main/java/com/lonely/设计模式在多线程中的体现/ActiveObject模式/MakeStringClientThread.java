package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/25 20:32
 * Description: makestring方法的客户端测试线程
 */
public class MakeStringClientThread extends Thread {

    private final ActiveObject activeObject;

    private final char buffer;

    public MakeStringClientThread(ActiveObject activeObject, char buffer) {
        this.activeObject = activeObject;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 1; true; i++) {
            Result result = this.activeObject.makeString(i, buffer);
            String realResult = (String) result.getResult();
            System.out.println(MessageFormat.format("线程{0}执行makeString:{1}", Thread.currentThread().getName(), realResult));

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
