package com.lonely.juc.原子操作.针对数组类型;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @auther: 15072
 * @date: 2020/2/29 14:50
 * Description: 整型数组 原子操作类api测试
 */
public class AtomicIntegerArrayTest {

    /**
     * 测试创建，获取，设置等api
     */
    @Test
    public void testCreateAndGetAndSet() {
        //测试创建，第一种：输入 数组长度
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(5);
        int index0Value = atomicIntegerArray.get(0);
        assertEquals(index0Value, 0);

        //测试创建，第二种，输入一个数组，源码中会clone一份当作value
        int[] newArray = {1, 2, 3};
        atomicIntegerArray = new AtomicIntegerArray(newArray);
        atomicIntegerArray.set(0, 10);
        assertEquals(newArray[0], 1);
        assertEquals(atomicIntegerArray.get(0), 10);

    }

    /**
     * 测试 先get，然后操作的一系列api
     */
    @Test
    public void testGetAndXx() {

        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(5);

        //测试 先返回指定i的值，然后将指定i对应的值 加1
        int andIncrement = atomicIntegerArray.getAndIncrement(0);
        assertEquals(andIncrement, 0);
        assertEquals(atomicIntegerArray.get(0), 1);

        //测试 先返回指定i的值，然后将指定i对应的值 减1
        atomicIntegerArray.set(0, 0);
        int andDecrement = atomicIntegerArray.getAndDecrement(0);
        assertEquals(andDecrement, 0);
        assertEquals(atomicIntegerArray.get(0), -1);

        //测试 先返回指定i的值，然后将指定i的值，加 n
        atomicIntegerArray.set(0, 0);
        int andAdd = atomicIntegerArray.getAndAdd(0, 10);
        assertEquals(andAdd, 0);
        assertEquals(atomicIntegerArray.get(0), 10);

        //测试 先返回指定i的值，然后重新赋值给指定i
        atomicIntegerArray.set(0, 0);
        int andSet = atomicIntegerArray.getAndSet(0, 10);
        assertEquals(andSet, 0);
        assertEquals(atomicIntegerArray.get(0), 10);

        //测试 先返回指定i的值，然后将指定i的值作为x，指定后续的函数操作后，将最终的值赋值给指定i的新值
        atomicIntegerArray.set(0, 0);
        int andUpdate = atomicIntegerArray.getAndUpdate(0, x -> x + 10);
        assertEquals(andUpdate, 0);
        assertEquals(atomicIntegerArray.get(0), 10);

        //测试 先返回指定i的值，然后将当前指定i的值作为x，10作为y，然后经过后续的函数操作，将最终的结果重新赋值给指定i
        atomicIntegerArray.set(0, 0);
        int andAccumulate = atomicIntegerArray.getAndAccumulate(0, 10, (x, y) -> x + y);
        assertEquals(andAccumulate, 0);
        assertEquals(atomicIntegerArray.get(0), 10);
    }

    /**
     * 测试 先操作看，然后返回值的一系列api操作
     */
    @Test
    public void testXxAndGet() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(5);

        //测试 先指定索引i的值进行加1，然后返回指定索引i的值
        int incrementAndGet = atomicIntegerArray.incrementAndGet(0);
        assertEquals(incrementAndGet, 1);
        assertEquals(atomicIntegerArray.get(0), 1);

        //测试 先将指定索引i的值进行减1，然后返回指定索引i的值
        atomicIntegerArray.set(0, 0);
        int decrementAndGet = atomicIntegerArray.decrementAndGet(0);
        assertEquals(decrementAndGet, -1);
        assertEquals(atomicIntegerArray.get(0), -1);

        //测试 先将指定索引i的值 加上 n，然后返回指定索引i的值
        atomicIntegerArray.set(0, 0);
        int addAndGet = atomicIntegerArray.addAndGet(0, -10);
        assertEquals(addAndGet, -10);
        assertEquals(atomicIntegerArray.get(0), -10);

        //测试 先将指定索引的值作为x，然后进行函数操作，将最新的值赋给索引i，且返回
        atomicIntegerArray.set(0, 0);
        int updateAndGet = atomicIntegerArray.updateAndGet(0, x -> x - 10);
        assertEquals(updateAndGet, -10);
        assertEquals(atomicIntegerArray.get(0), -10);

        //测试 先将指定索引i的值作为x，将10作为y，进行函数操作后，将最终的值赋值给索引i，且返回
        atomicIntegerArray.set(0, 0);
        int accumulateAndGet = atomicIntegerArray.accumulateAndGet(0, 10, (x, y) -> x - y);
        assertEquals(accumulateAndGet, -10);
        assertEquals(atomicIntegerArray.get(0), -10);
    }

    /**
     * 测试 compareAndSet(x,y,z)方法,如何指定索引x的值 == y，则将指定索引x的值 设置为 z
     */
    @Test
    public void testCompareAndSet() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(1);
        boolean compareAndSet = atomicIntegerArray.compareAndSet(0, 0, 100);
        assertTrue(compareAndSet);
        assertEquals(atomicIntegerArray.get(0), 100);
    }

}
