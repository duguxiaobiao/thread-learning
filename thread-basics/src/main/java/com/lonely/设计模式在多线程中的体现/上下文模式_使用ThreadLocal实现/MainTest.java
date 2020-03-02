package com.lonely.设计模式在多线程中的体现.上下文模式_使用ThreadLocal实现;

/**
 * @auther: 15072
 * @date: 2020/2/24 14:13
 * Description: 测试类
 */
public class MainTest {

    public static void main(String[] args) {

        QueryIdCardFromRemoteService queryIdCardFromRemoteService = new QueryIdCardFromRemoteService();
        QueryNameFromDbService queryNameFromDbService = new QueryNameFromDbService();

        for (int i = 0; i < 5; i++) {
            new Thread(new ExecuteTask(queryNameFromDbService,queryIdCardFromRemoteService)).start();
        }

    }


}
