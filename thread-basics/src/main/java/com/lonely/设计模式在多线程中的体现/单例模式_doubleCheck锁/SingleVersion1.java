package com.lonely.设计模式在多线程中的体现.单例模式_doubleCheck锁;

/**
 * @auther: 15072
 * @date: 2020/2/22 13:25
 * Description: 单例模式第一种---饿汉式，线程安全的
 */
public class SingleVersion1 {

    /**
     * 私有对象，在主动使用 SingleVersion1时就会创建，且只创建1次，线程安全
     */
    private static SingleVersion1 singleVersion1 = new SingleVersion1();

    private SingleVersion1(){

    }

    /**
     * 返回对象
     * @return
     */
    public static SingleVersion1 getInstance(){
        return singleVersion1;
    }

}
