package com.lonely.设计模式在多线程中的体现.WorkThread模式;

/**
 * @auther: 15072
 * @date: 2020/2/25 16:22
 * Description: 测试类
 */
public class MainTest {

    public static void main(String[] args) {

        Channel channel = new Channel(5);
        channel.startWorker();

        CarryThread carryThread = new CarryThread("搬运员工A",channel);
        CarryThread carryThread2 = new CarryThread("搬运员工B",channel);
        CarryThread carryThread3 = new CarryThread("搬运员工C",channel);
        CarryThread carryThread4 = new CarryThread("搬运员工D",channel);
        CarryThread carryThread5 = new CarryThread("搬运员工E",channel);

        carryThread.start();
        carryThread2.start();
        carryThread3.start();
        carryThread4.start();
        carryThread5.start();

    }
}
