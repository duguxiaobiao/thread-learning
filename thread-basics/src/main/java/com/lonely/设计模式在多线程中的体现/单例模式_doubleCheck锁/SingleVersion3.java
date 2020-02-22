package com.lonely.设计模式在多线程中的体现.单例模式_doubleCheck锁;

/**
 * @auther: 15072
 * @date: 2020/2/22 13:34
 * Description: 线程比较安全的懒加载方式---double check 锁方式，虽然是线程安全的，但是可能存在空指针异常，
 * 取决于该对象的实例化时候，是否还依赖了其他的对象
 */
public class SingleVersion3 {

    /**
     * 设置为 volatile后，即可保证禁止重排序，避免空指针异常。
     */
    private volatile static SingleVersion3 singleVersion3;

    //举个例子，假设该对象，是在SingleVersion3的构造方法中实例化，那么可能存在在多线程情况下，a线程刚刚实例化，object对象还没
    //开辟完空间，另一个线程发现实例化对象不为空了，就直接拿去使用了，就会造成空指针异常。
    // 如果想要解决问题，只需要将 实例化的对象设置为 volatile的即可
    private static Object object;

    private SingleVersion3() {

    }

    public static SingleVersion3 getInstance() {
        if (singleVersion3 == null) {
            synchronized (SingleVersion3.class) {
                //这里需要再检查一次，因为在多线程情况下，如果这里不检查，虽然第一个拿到锁的线程实例化了一个对象，
                //释放锁后，另一个抢到锁的线程就会没有经过判断，再次实例化一次。
                if (singleVersion3 == null) {
                    singleVersion3 = new SingleVersion3();
                }
            }
        }
        return null;
    }
}
