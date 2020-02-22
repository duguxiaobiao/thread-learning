package com.lonely.设计模式在多线程中的体现.单例模式_doubleCheck锁;

/**
 * @auther: 15072
 * @date: 2020/2/22 13:30
 * Description: 饱汉式------在需要使用的时候初始化,线程不安全
 */
public class SingleVersion2 {

    private static SingleVersion2 singleVersion2;

    private SingleVersion2() {

    }

    /**
     * 当在调用该方法时，才会初始化 对象，使用懒加载方式，在单线程情况安全，如果在多线程情况下，线程不安全。
     * 比如，多线程情况下，A线程判断==null，就会实例化一个,B线程同时判断也等于null，同样也会去实例化一个，这样就不是同一个对象了
     *
     * @return
     */
    public static SingleVersion2 getInstance() {
        if (singleVersion2 == null) {
            singleVersion2 = new SingleVersion2();
        }
        return singleVersion2;
    }


}
