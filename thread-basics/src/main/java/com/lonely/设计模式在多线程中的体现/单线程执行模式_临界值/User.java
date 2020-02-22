package com.lonely.设计模式在多线程中的体现.单线程执行模式_临界值;

/**
 * @auther: 15072
 * @date: 2020/2/22 15:38
 * Description: 用户
 */
public class User extends Thread {

    private String name;

    private String address;

    private Gate gate;

    public User(String name, String address, Gate gate) {
        this.name = name;
        this.address = address;
        this.gate = gate;
    }


    @Override
    public void run() {
        while (true) {
            this.gate.pass(this.name, this.address);
        }
    }
}
