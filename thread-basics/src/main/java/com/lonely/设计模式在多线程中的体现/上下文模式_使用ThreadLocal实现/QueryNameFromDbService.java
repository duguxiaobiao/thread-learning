package com.lonely.设计模式在多线程中的体现.上下文模式_使用ThreadLocal实现;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/24 14:04
 * Description: 假设程序中有一个从数据库中查询名称的服务
 */
public class QueryNameFromDbService {

    /**
     * 查询用户名称
     * @return
     */
    public void queryName(){
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = ActionContext.getInstance().getUser();
        user.setName(MessageFormat.format("{0}--{1}", Thread.currentThread().getName(),"lonely"));
    }

}
