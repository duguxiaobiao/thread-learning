package com.lonely.设计模式在多线程中的体现.观察者模式_监听线程状态;

import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/20 17:51
 * Description: 测试类
 */
public class MainTest {


    public static void main(String[] args) {

        //定义观察者
        CustomerObServer customerObServer = new CustomerObServer();

        //定义待观察者，同时观察10个线程
        for (int i = 0; i < 10; i++) {
            int curr = i;
            new Thread(new CustomerSubject(customerObServer) {
                //重写run方法，添加监听
                @Override
                public void run() {
                    this.changeNotify(new ListenerResource(this, ThreadState.RUNNING, null));

                    try {
                        if (curr % 2 == 0) {
                            //奇数，正常运行
                            TimeUnit.SECONDS.sleep(1);
                        } else {
                            //偶数，故意报错
                            System.out.println(1 / 0);
                        }
                    } catch (Exception e) {
                        this.changeNotify(new ListenerResource(this, ThreadState.ERROR, e));
                        return;
                    }

                    this.changeNotify(new ListenerResource(this, ThreadState.DOWN, null));
                }
            }, "线程--" + curr).start();
        }


    }


}
