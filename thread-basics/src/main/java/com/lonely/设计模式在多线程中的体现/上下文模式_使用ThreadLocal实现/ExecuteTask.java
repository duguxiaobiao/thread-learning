package com.lonely.设计模式在多线程中的体现.上下文模式_使用ThreadLocal实现;

import java.text.MessageFormat;

/**
 * @auther: 15072
 * @date: 2020/2/24 14:07
 * Description: 执行操作的线程
 */
public class ExecuteTask implements Runnable {

    private QueryNameFromDbService queryNameFromDbService;
    private QueryIdCardFromRemoteService queryIdCardFromRemoteService;

    public ExecuteTask(QueryNameFromDbService queryNameFromDbService, QueryIdCardFromRemoteService queryIdCardFromRemoteService) {
        this.queryNameFromDbService = queryNameFromDbService;
        this.queryIdCardFromRemoteService = queryIdCardFromRemoteService;
    }

    @Override
    public void run() {
        //User user = new User();
        this.queryNameFromDbService.queryName();
        this.queryIdCardFromRemoteService.getIdCardByName();
        User user = ActionContext.getInstance().getUser();
        //输出
        System.out.println(MessageFormat.format("本次任务获取到name:{0},idCard:{1}", user.getName(), user.getIdCard()));

        //清除数据
        ActionContext.getInstance().clear();

    }
}
