package com.lonely.juc.原子操作.针对引用类型;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @auther: 15072
 * @date: 2020/2/29 13:30
 * Description: 引入了版本号机制的cas实现，解决了cas导致的ABA问题
 */
public class AtomicStampedReferenceTest {

    /**
     * 测试创建，获取，和赋值操作
     */
    @Test
    public void testCreateAndGetAndSet() {
        //测试创建 hello--初始值  1--初始版本号
        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("hello", 1);

        //测试获取值的第一种方式：借助 int[]类型数组，长度必须大于0，则调用get()方法，会将版本号放入到数组的下标为0的索引中，返回值就是value值。这样就可以同时拿到值和版本号
        int[] stamp = new int[1];
        String currReferenceValue = atomicStampedReference.get(stamp);
        assertEquals(stamp[0], 1);
        assertEquals(currReferenceValue, "hello");

        //测试 获取值的第二种方式
        String reference = atomicStampedReference.getReference();
        int referenceStamp = atomicStampedReference.getStamp();
        assertEquals(reference, "hello");
        assertEquals(referenceStamp, 1);

        //测试 set操作
        atomicStampedReference.set("world", 2);
        assertEquals(atomicStampedReference.getReference(), "world");
        assertEquals(atomicStampedReference.getStamp(), 2);
    }


    /**
     * 测试 attempStamp(x,newStamp),尝试将当前value值的版本号设置为新的版本号,前提是传入的x == this.getReference();
     */
    @Test
    public void testAttempStamp() {
        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("hello", 1);
        boolean hello = atomicStampedReference.attemptStamp("hello", 2);
        assertTrue(hello);
        assertEquals(atomicStampedReference.getStamp(), 2);
    }


    /**
     * 测试 compareAndSet()方法
     */
    @Test
    public void testCompareAndSet() {
        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("hello", 1);
        boolean compareAndSet = atomicStampedReference.compareAndSet("hello", "world", 1, 2);
        assertTrue(compareAndSet);
        assertEquals(atomicStampedReference.getReference(), "world");
        assertEquals(atomicStampedReference.getStamp(), 2);
    }
}
