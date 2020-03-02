package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/25 20:37
 * Description: diaplay 客户端线程测试
 */
public class DisplayClientThread extends Thread {

    private ActiveObject activeObject;

    public DisplayClientThread(ActiveObject activeObject) {
        this.activeObject = activeObject;
    }

    @Override
    public void run() {
        for (int i = 0; true; i++) {
            String text = Thread.currentThread().getName() + "----" + i;
            this.activeObject.displayString(text);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
