package com.lonely.设计模式在多线程中的体现.单线程执行模式_临界值;

import java.text.MessageFormat;

/**
 * @auther: 15072
 * @date: 2020/2/22 15:37
 * Description: 门 对象
 */
public class Gate {

    /**
     * 经过门的次数
     */
    private int count;

    private String name;

    private String address;


    /**
     * 针对共享资源，需要串行化处理，加锁
     *
     * @param name
     * @param address
     */
    public synchronized void pass(String name, String address) {
        count++;
        this.name = name;
        this.address = address;
        check();
    }


    /**
     * 验证用户名和地址是否匹配
     */
    private void check() {
        if (this.name.charAt(0) != this.address.charAt(0)) {
            System.out.println(MessageFormat.format("check error,count:[{0}]user:[{1}]", this.count, this.toString()));
        }
    }


    @Override
    public String toString() {
        return "Gate{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
