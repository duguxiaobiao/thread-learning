package com.lonely.juc.模拟基于cas实现的原子类引发的ABA问题;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @auther: 15072
 * @date: 2020/2/29 12:57
 * Description: 基于cas实现的原子类 而 引发的aba问题模拟出现
 */
public class AtomicABAProblemsByCas {


    public static void main(String[] args) throws InterruptedException {

        AtomicReference<String> atomicReference = new AtomicReference<>("A");
        new Thread(() -> {
            String currentValue = atomicReference.get();
            System.out.println(MessageFormat.format("线程：{0}获取当前value值：{1}", Thread.currentThread().getName(), currentValue));

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //比较替换
            boolean compareAndSet = atomicReference.compareAndSet(currentValue, "B");
            if (compareAndSet) {
                //更新成功
                System.out.println(MessageFormat.format("线程：{0} 成功将值：{1}===》{2}", Thread.currentThread().getName(), currentValue, "B"));
            } else {
                //更新失败
                System.out.println(MessageFormat.format("线程：{0} 失败将值：{1}===》{2}", Thread.currentThread().getName(), currentValue, "B"));
            }
        }).start();

        TimeUnit.MILLISECONDS.sleep(100);

        new Thread(() -> {
            String currentValue = atomicReference.get();
            System.out.println(MessageFormat.format("线程：{0}获取当前value值：{1}", Thread.currentThread().getName(), currentValue));

            //更新
            boolean compareAndSet = atomicReference.compareAndSet(currentValue, "B");
            if (compareAndSet) {
                System.out.println(MessageFormat.format("线程：{0} 成功将值：{1}===》{2}", Thread.currentThread().getName(), currentValue, "B"));
                boolean compareAndSet1 = atomicReference.compareAndSet("B", currentValue);
                if (compareAndSet1) {
                    System.out.println(MessageFormat.format("线程：{0} 成功将值：{1}===》{2}", Thread.currentThread().getName(), "B", currentValue));
                } else {
                    System.out.println(MessageFormat.format("线程：{0} 失败将值：{1}===》{2}", Thread.currentThread().getName(), "B", currentValue));
                }
            } else {
                System.out.println(MessageFormat.format("线程：{0} 失败将值：{1}===》{2}", Thread.currentThread().getName(), currentValue, "B"));
            }

        }).start();


    }

}
