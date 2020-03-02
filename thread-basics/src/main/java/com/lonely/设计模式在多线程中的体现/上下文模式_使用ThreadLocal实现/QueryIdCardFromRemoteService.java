package com.lonely.设计模式在多线程中的体现.上下文模式_使用ThreadLocal实现;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/24 14:06
 * Description: 查询身份证号，从远程服务中获取
 */
public class QueryIdCardFromRemoteService {


    public void getIdCardByName(){
        try {
            //模拟根据用户名称获取对应的身份证号
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //假设需要用户名
        User user = ActionContext.getInstance().getUser();
        String name = user.getName();
        user.setIdCard(MessageFormat.format("{0}----{1}", Thread.currentThread().getName(),
                Thread.currentThread().getId()));
    }

}
