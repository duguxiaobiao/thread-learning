package com.lonely.设计模式在多线程中的体现.WorkThread模式;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/25 16:11
 * Description: 组装商品员工线程
 */
public class WorkerThread extends Thread {

    /**
     * 员工名称
     */
    private String name;

    /**
     * 传送带对象
     */
    private Channel channel;

    private Random random = new Random(System.currentTimeMillis());

    public WorkerThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }


    @Override
    public void run() {
        while (true) {
            Goods take = this.channel.take();
            System.out.println(MessageFormat.format("组装商品员工：{0}组装商品：{1}", getName(), take.getName()));

            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
