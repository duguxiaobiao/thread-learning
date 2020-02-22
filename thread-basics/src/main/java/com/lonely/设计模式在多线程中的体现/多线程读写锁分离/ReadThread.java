package com.lonely.设计模式在多线程中的体现.多线程读写锁分离;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/22 20:03
 * Description: 读线程
 */
public class ReadThread extends Thread {

    private ReadAndWriteLock readAndWriteLock;

    private char[] buffer;

    public ReadThread(ReadAndWriteLock readAndWriteLock, char[] buffer) {
        this.readAndWriteLock = readAndWriteLock;
        this.buffer = buffer;
    }


    @Override
    public void run() {

        while (true) {
            try {
                readAndWriteLock.readLock();
                //将数据输出
                StringBuilder msg = new StringBuilder(Thread.currentThread().getName()).append(": reading --");
                for (int i = 0; i < buffer.length; i++) {
                    msg.append(buffer[i]);
                }
                System.out.println(msg.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readAndWriteLock.readUnLock();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
