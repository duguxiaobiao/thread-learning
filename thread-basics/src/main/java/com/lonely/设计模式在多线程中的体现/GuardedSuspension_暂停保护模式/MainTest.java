package com.lonely.设计模式在多线程中的体现.GuardedSuspension_暂停保护模式;

import java.util.LinkedList;

/**
 * @auther: 15072
 * @date: 2020/2/23 17:06
 * Description:
 */
public class MainTest {


    public static void main(String[] args) {

        LinkedList<Request> requests = new LinkedList<>();

        //两个发任务
        new RequestClient(requests).start();
        new RequestClient(requests).start();

        //1个处理任务
        new RequestService(requests).start();

    }
}
