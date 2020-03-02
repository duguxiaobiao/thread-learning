package com.lonely.juc.原子操作.针对基础数据类型;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

/**
 * @auther: 15072
 * @date: 2020/2/28 20:27
 * Description: 针对long类型的原子操作类api测试
 */
public class AtomicLongTest {

    /**
     * 测试创建，获取和设值操作api
     */
    @Test
    public void testCreateAndGetAndSet() {

        //测试无参构造，默认值为0
        AtomicLong atomicLong = new AtomicLong();
        assertEquals(atomicLong.get(), 0);

        //测试带参构造
        atomicLong = new AtomicLong(100L);
        assertEquals(atomicLong.get(), 100);

        //测试 set方法
        atomicLong.set(200);
        assertEquals(atomicLong.get(), 200);
    }

    /**
     * 测试 先get，后操作的一系列api
     */
    @Test
    public void testGetAndXX() {
        AtomicLong atomicLong = new AtomicLong();

        // i++, 先返回value，然后 + 1
        long andIncrement = atomicLong.getAndIncrement();
        assertEquals(andIncrement, 0);
        assertEquals(atomicLong.get(), 1);

        //i--,先返回value，然后 -1
        atomicLong.set(0);
        long andDecrement = atomicLong.getAndDecrement();
        assertEquals(andDecrement, 0);
        assertEquals(atomicLong.get(), -1);

        //先获取value值，然后设值为指定值
        atomicLong.set(0);
        long andSet = atomicLong.getAndSet(100);
        assertEquals(andSet, 0);
        assertEquals(atomicLong.get(), 100);

        //先返回value，然后将当前value，根据指定函数进行操作，将最终值设置为新值
        atomicLong.set(0);
        long andUpdate = atomicLong.getAndUpdate(x -> x + 5);
        assertEquals(andUpdate, 0);
        assertEquals(atomicLong.get(), 5);

        //先返回当前value值，然后将当前的value值-->x,10--->y,然后进行x-y的操作，将新值设置为最新的value
        atomicLong.set(0);
        long andAccumulate = atomicLong.getAndAccumulate(10, (x, y) -> x - y);
        assertEquals(andAccumulate, 0);
        assertEquals(atomicLong.get(), -10);
    }


    /**
     * 测试 先操作，然后返回最新的value的一系列操作
     */
    @Test
    public void testXXAndGet() {
        AtomicLong atomicLong = new AtomicLong();

        // ++i; 先加1，然后返回
        long incrementAndGet = atomicLong.incrementAndGet();
        assertEquals(incrementAndGet, 1);
        assertEquals(atomicLong.get(), 1);

        // --i;先减1，然后返回
        atomicLong.set(0);
        long decrementAndGet = atomicLong.decrementAndGet();
        assertEquals(decrementAndGet, -1);
        assertEquals(atomicLong.get(), -1);

        //先加n，然后返回
        atomicLong.set(0);
        long addAndGet = atomicLong.addAndGet(10);
        assertEquals(addAndGet, 10);
        assertEquals(atomicLong.get(), 10);

        //将当前value的值，根据指定函数转换后，将结果设置为value值，返回
        atomicLong.set(0);
        long updateAndGet = atomicLong.updateAndGet(x -> x - 5);
        assertEquals(updateAndGet, -5);
        assertEquals(atomicLong.get(), -5);

        //先将当前value的值赋值给x，将入参20设值为y，然后根据函数将最终结果设值给value，返回
        atomicLong.set(0);
        long accumulateAndGet = atomicLong.accumulateAndGet(20, (x, y) -> x + y);
        assertEquals(accumulateAndGet, 20);
        assertEquals(atomicLong.get(), 20);
    }

    /**
     * 测试 compareAndSet(x,y)方法，比较，如何参数x==this.value,则 this.value = y;
     */
    @Test
    public void testCompareAndSet() {
        AtomicLong atomicLong = new AtomicLong();
        boolean compareAndSet = atomicLong.compareAndSet(0, 100);
        assertTrue(compareAndSet);
        assertEquals(atomicLong.get(), 100);
    }

}
