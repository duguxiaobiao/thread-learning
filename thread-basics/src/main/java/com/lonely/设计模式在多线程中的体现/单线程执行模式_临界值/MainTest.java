package com.lonely.设计模式在多线程中的体现.单线程执行模式_临界值;

/**
 * @auther: 15072
 * @date: 2020/2/22 15:50
 * Description:
 */
public class MainTest {

    public static void main(String[] args) {
        Gate gate = new Gate();
        for (int i = 0; i < 10; i++) {
            new User("H_dalao", "Hubei", gate).start();
            new User("S_dalao", "Shanghai", gate).start();
            new User("B_dalao", "Beijing", gate).start();
        }


    }

}
