package com.lonely.设计模式在多线程中的体现.WorkThread模式;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/25 16:12
 * Description:搬运工人线程
 */
public class CarryThread extends Thread {

    /**
     * 搬运工名称
     */
    private String name;

    /**
     * 传送带
     */
    private Channel channel;

    private Random random = new Random(System.currentTimeMillis());

    public CarryThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }


    @Override
    public void run() {

        for (int i = 1; true; i++) {
            Goods goods = new Goods("商品：" + Thread.currentThread().getId() + i);
            channel.put(goods);
            System.out.println(MessageFormat.format("{0}放入商品：{1}到传送带", getName(), goods.getName()));
            //随机休息一段时间
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
