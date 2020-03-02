package com.lonely.juc.模拟基于cas实现的原子类引发的ABA问题;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @auther: 15072
 * @date: 2020/2/29 13:12
 * Description: 解决 cas引发的aba问题
 */
public class ResolveTheAbaProblemsByCas {


    public static void main(String[] args) throws InterruptedException {
        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("A", 0);
        //AtomicReference<String> atomicReference = new AtomicReference<>("A");
        new Thread(() -> {
            String currentValue = atomicStampedReference.getReference();
            //版本号
            int stamp = atomicStampedReference.getStamp();
            System.out.println(MessageFormat.format("线程：{0}获取当前value值：{1}", Thread.currentThread().getName(), currentValue));

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //比较替换
            boolean compareAndSet = atomicStampedReference.compareAndSet(currentValue, "B", stamp, stamp + 1);
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
            String currentValue = atomicStampedReference.getReference();
            int stamp = atomicStampedReference.getStamp();
            System.out.println(MessageFormat.format("线程：{0}获取当前value值：{1}", Thread.currentThread().getName(), currentValue));

            //更新
            int currStamp = stamp + 1;
            boolean compareAndSet = atomicStampedReference.compareAndSet(currentValue, "B", stamp, currStamp);
            if (compareAndSet) {
                System.out.println(MessageFormat.format("线程：{0} 成功将值：{1}===》{2}", Thread.currentThread().getName(), currentValue, "B"));

                boolean compareAndSet1 = atomicStampedReference.compareAndSet("B", currentValue, currStamp, currStamp + 1);
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
