package com.lonely.设计模式在多线程中的体现.多线程读写锁分离;

/**
 * @auther: 15072
 * @date: 2020/2/22 20:18
 * Description: 测试类
 */
public class MainTest {


    public static void main(String[] args) {

        ReadAndWriteLock readAndWriteLock = new ReadAndWriteLock();

        //初始化
        char[] buffer = new char[10];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = '*';
        }

        new ReadThread(readAndWriteLock,buffer).start();
        new ReadThread(readAndWriteLock,buffer).start();
        new ReadThread(readAndWriteLock,buffer).start();
        new ReadThread(readAndWriteLock,buffer).start();
        new ReadThread(readAndWriteLock,buffer).start();
        new ReadThread(readAndWriteLock,buffer).start();
        new ReadThread(readAndWriteLock,buffer).start();
        new ReadThread(readAndWriteLock,buffer).start();

        new WriteThread(readAndWriteLock,buffer).start();
        new WriteThread(readAndWriteLock,buffer).start();
        new WriteThread(readAndWriteLock,buffer).start();
        new WriteThread(readAndWriteLock,buffer).start();

    }


}
