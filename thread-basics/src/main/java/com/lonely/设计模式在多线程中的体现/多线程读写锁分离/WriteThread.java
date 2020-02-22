package com.lonely.设计模式在多线程中的体现.多线程读写锁分离;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/22 20:13
 * Description: 写线程
 */
public class WriteThread extends Thread {

    private ReadAndWriteLock readAndWriteLock;

    private char[] buffer;

    private String randomStr = "abcdefg";
    private Random random = new Random();


    public WriteThread(ReadAndWriteLock readAndWriteLock, char[] buffer) {
        this.readAndWriteLock = readAndWriteLock;
        this.buffer = buffer;
    }


    @Override
    public void run() {
        while (true){
            try {
                readAndWriteLock.writeLock();
                int index = this.random.nextInt(randomStr.length());
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = this.randomStr.charAt(index);
                }
                System.out.println(MessageFormat.format("{0}: writing--", Thread.currentThread().getName()));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readAndWriteLock.writeUnLock();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
