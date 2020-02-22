package com.lonely.设计模式在多线程中的体现.单例模式_doubleCheck锁;

/**
 * @auther: 15072
 * @date: 2020/2/22 13:54
 * Description: 使用枚举的方式--业内推荐最优雅的单例方式，线程安全
 */
public class SingleVersion5 {

    private SingleVersion5() {

    }

    public static SingleVersion5 getInstance() {
        return SingleEnum.SINGLE.getInstance();
    }


    private enum SingleEnum {

        SINGLE;

        private final SingleVersion5 singleVersion5;

        SingleEnum() {
            this.singleVersion5 = new SingleVersion5();
        }

        public SingleVersion5 getInstance() {
            return singleVersion5;
        }
    }

}
