package com.lonely.设计模式在多线程中的体现.单例模式_doubleCheck锁;

/**
 * @auther: 15072
 * @date: 2020/2/22 13:44
 * Description: 使用内部类的方式，线程安全的--既保证了懒加载，即只有在调用getInstance()时才会实例化，
 * 又保证了线程安全，即实例对象只有在第一次主动使用SingleInnerClass类时才会创建。
 */
public class SingleVersion4 {

    private SingleVersion4() {

    }

    public static SingleVersion4 getInstance() {
        return SingleInnerClass.singleVersion4;
    }


    private static class SingleInnerClass {
        public static SingleVersion4 singleVersion4 = new SingleVersion4();
    }


}
