package com.lonely.juc.原子操作.针对数组类型;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * @auther: 15072
 * @date: 2020/2/29 15:36
 * Description: 基于long类型数组的原子操作类 测试类
 */
public class AtomicLongArrayTest {

    /**
     * 测试 创建，获取，设置等api
     */
    @Test
    public void testCreateAndGetAndSet() {

        //第一种构造,传入数组的长度
        AtomicLongArray atomicLongArray = new AtomicLongArray(1);
        long index0Value = atomicLongArray.get(0);
        assertEquals(index0Value, 0);

        //第二种构造,传入数组
        long[] newArray = {1, 2, 3};
        atomicLongArray = new AtomicLongArray(newArray);
        assertEquals(atomicLongArray.get(0), 1);

        //测试 set方法
        atomicLongArray.set(0, 100);
        assertEquals(atomicLongArray.get(0), 100);
    }

    /**
     * 测试 先返回，再操作等一系列api测试
     */
    @Test
    public void testGetAndXx() {

        AtomicLongArray atomicLongArray = new AtomicLongArray(1);

        //测试 先返回指定索引的值，然后加1
        long andIncrement = atomicLongArray.getAndIncrement(0);
        assertEquals(andIncrement, 0);
        assertEquals(atomicLongArray.get(0), 1);

        //测试 先返回指定索引的值，然后减1
        atomicLongArray.set(0, 0);
        long andDecrement = atomicLongArray.getAndDecrement(0);
        assertEquals(andDecrement, 0);
        assertEquals(atomicLongArray.get(0), -1);

        //测试 先返回指定索引的值，然后加 n
        atomicLongArray.set(0, 0);
        long andAdd = atomicLongArray.getAndAdd(0, 100);
        assertEquals(andAdd, 0);
        assertEquals(atomicLongArray.get(0), 100);

        //测试 先返回指定索引的值，然后 直接赋值
        atomicLongArray.set(0, 0);
        long andSet = atomicLongArray.getAndSet(0, 100);
        assertEquals(andSet, 0);
        assertEquals(atomicLongArray.get(0), 100);

        //测试 先返回指定索引的值，然后将当前索引i的值作为x，经过后续的函数操作后，将最新的值赋值给索引i
        atomicLongArray.set(0, 0);
        long andUpdate = atomicLongArray.getAndUpdate(0, x -> x + 10);
        assertEquals(andUpdate, 0);
        assertEquals(atomicLongArray.get(0), 10);

        //测试 先返回指定索引i的值，然后将当前索引i的值作为x，10作为y，经过后续的操作后，将最终的值赋值给索引i
        atomicLongArray.set(0, 0);
        long andAccumulate = atomicLongArray.getAndAccumulate(0, 10, (x, y) -> x + y);
        assertEquals(andAccumulate, 0);
        assertEquals(atomicLongArray.get(0), 10);
    }

    /**
     * 测试 先操作，后返回的一系列api操作
     */
    @Test
    public void testXxAndGet() {

        AtomicLongArray atomicLongArray = new AtomicLongArray(1);

        //测试 先将指定索引i的值加1， 然后重新赋值给i，且返回
        long incrementAndGet = atomicLongArray.incrementAndGet(0);
        assertEquals(incrementAndGet, 1);
        assertEquals(atomicLongArray.get(0), 1);

        //测试 先将指定索引i的值减1， 然后重新赋值给i，且返回
        atomicLongArray.set(0, 0);
        long decrementAndGet = atomicLongArray.decrementAndGet(0);
        assertEquals(decrementAndGet, -1);
        assertEquals(atomicLongArray.get(0), -1);

        //测试 先将指定索引i的值 加 n，然后重新赋值给i，且返回
        atomicLongArray.set(0, 0);
        long addAndGet = atomicLongArray.addAndGet(0, -10);
        assertEquals(addAndGet, -10);
        assertEquals(atomicLongArray.get(0), -10);

        //测试 先将指定索引i的值作为x，然后执行后续的函数操作，将最终的结果重新赋值给i，且返回
        atomicLongArray.set(0, 0);
        long updateAndGet = atomicLongArray.updateAndGet(0, x -> x - 10);
        assertEquals(updateAndGet, -10);
        assertEquals(atomicLongArray.get(0), -10);

        //测试 先将指定索引i的值作为x，将100作为y，然后执行后面的函数操作，将最终的结果重新赋值给i，且返回
        atomicLongArray.set(0, 0);
        long accumulateAndGet = atomicLongArray.accumulateAndGet(0, 100, (x, y) -> x - y);
        assertEquals(accumulateAndGet, -100);
        assertEquals(atomicLongArray.get(0), -100);
    }

    /**
     * 测试 compareAndSet();
     */
    @Test
    public void testCompareAndSet() {
        AtomicLongArray atomicLongArray = new AtomicLongArray(1);
        boolean compareAndSet = atomicLongArray.compareAndSet(0, 0, 100);
        assertTrue(compareAndSet);
        assertEquals(atomicLongArray.get(0), 100);
    }
}
