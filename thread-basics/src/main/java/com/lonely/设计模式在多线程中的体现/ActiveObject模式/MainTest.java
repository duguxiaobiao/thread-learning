package com.lonely.设计模式在多线程中的体现.ActiveObject模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 20:31
 * Description: 客户端测试
 */
public class MainTest {

    public static void main(String[] args) {
        ActiveObjectProxy activeObject = ActiveObjectFactory.createActiveObject();

        new MakeStringClientThread(activeObject, 'A').start();
        new MakeStringClientThread(activeObject,'B').start();

        new DisplayClientThread(activeObject).start();
        new DisplayClientThread(activeObject).start();

    }

}
