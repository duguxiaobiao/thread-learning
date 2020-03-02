package com.lonely.juc.原子操作.针对引用类型中指定属性类型;

import com.sun.corba.se.impl.oa.poa.AOMEntry;
import lombok.Getter;

import static org.junit.Assert.*;

import org.junit.Test;
import sun.plugin2.ipc.unix.UnixIPCFactory;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @auther: 15072
 * @date: 2020/2/29 21:02
 * Description: 针对引用类型中的某一个int类型变量（不能是private，且必须是 volatile修饰）
 */
public class AtomicIntegerFieldUpdaterTest {

    @Getter
    class Simple {
        public volatile int age;
    }

    /**
     * 测试 创建，获取，和设置api
     */
    @Test
    public void testCreateAndSetAndGet() {

        //测试 创建
        AtomicIntegerFieldUpdater<Simple> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Simple.class, "age");

        //测试 set
        Simple simple = new Simple();
        atomicIntegerFieldUpdater.set(simple, 10);

        //测试 get方法
        int i = atomicIntegerFieldUpdater.get(simple);
        assertEquals(i, 10);
    }

    /**
     * 测试 先返回，再操作的一系列api
     */
    @Test
    public void testGetAndXx() {
        AtomicIntegerFieldUpdater<Simple> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Simple.class, "age");
        Simple simple = new Simple();
        atomicIntegerFieldUpdater.set(simple, 100);

        //测试 先获取指定对象的属性值，然后在将属性值 加1
        int andIncrement = atomicIntegerFieldUpdater.getAndIncrement(simple);
        assertEquals(andIncrement, 100);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 101);

        //测试 先获取指定对象的属性值，然后在将属性值 减1
        atomicIntegerFieldUpdater.set(simple, 100);
        int andDecrement = atomicIntegerFieldUpdater.getAndDecrement(simple);
        assertEquals(andDecrement, 100);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 99);

        //测试 先获取指定对象的属性值，然后加n
        atomicIntegerFieldUpdater.set(simple, 100);
        int andAdd = atomicIntegerFieldUpdater.getAndAdd(simple, 100);
        assertEquals(andAdd, 100);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 200);

        //测试 先获取指定对象的值，然后赋予新值
        atomicIntegerFieldUpdater.set(simple, 100);
        int andSet = atomicIntegerFieldUpdater.getAndSet(simple, 200);
        assertEquals(andSet, 100);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 200);

        //测试 先获取指定对象的值，然后将当前对象的值作为x，进行后续函数的操作，将最终的结果重新赋值给对象
        atomicIntegerFieldUpdater.set(simple, 100);
        int andUpdate = atomicIntegerFieldUpdater.getAndUpdate(simple, x -> x + 10);
        assertEquals(andUpdate, 100);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 110);

        //测试 先获取指定对象的值，然后将指定对象的值作为x，10作为y，进行函数操作后，将最终的结果再重新赋予对象
        atomicIntegerFieldUpdater.set(simple, 100);
        int andAccumulate = atomicIntegerFieldUpdater.getAndAccumulate(simple, 10, (x, y) -> x - y);
        assertEquals(andAccumulate, 100);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 90);
    }

    /**
     * 测试 先操作，后返回的一系列api操作
     */
    @Test
    public void testXxAndGet() {
        AtomicIntegerFieldUpdater<Simple> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Simple.class, "age");
        Simple simple = new Simple();
        atomicIntegerFieldUpdater.set(simple, 100);

        //测试 先将指定对象的值，加1，重新赋给指定对象，然后返回
        int incrementAndGet = atomicIntegerFieldUpdater.incrementAndGet(simple);
        assertEquals(incrementAndGet, 101);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 101);

        //测试 先将指定对象的值，减1，重新赋给指定对象，然后返回
        atomicIntegerFieldUpdater.set(simple, 100);
        int decrementAndGet = atomicIntegerFieldUpdater.decrementAndGet(simple);
        assertEquals(decrementAndGet, 99);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 99);

        //测试 先将指定对象的值，加n，重新赋给指定对象，然后返回
        atomicIntegerFieldUpdater.set(simple, 100);
        int addAndGet = atomicIntegerFieldUpdater.addAndGet(simple, 20);
        assertEquals(addAndGet, 120);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 120);

        //测试 先将指定对象的值作为x，执行后续函数操作后，重新赋给对象，且返回
        atomicIntegerFieldUpdater.set(simple, 100);
        int updateAndGet = atomicIntegerFieldUpdater.updateAndGet(simple, x -> x - 20);
        assertEquals(updateAndGet, 80);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 80);

        //测试 先将指定对象的值作为x，20作为y，然后执行后续函数操作，将最终的结果重新赋给指定对象，且返回
        atomicIntegerFieldUpdater.set(simple, 100);
        int accumulateAndGet = atomicIntegerFieldUpdater.accumulateAndGet(simple, 20, (x, y) -> x - y);
        assertEquals(accumulateAndGet, 80);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 80);
    }

    /**
     * 测试 compareAndSet()方法
     */
    @Test
    public void testCompareAndSet() {
        AtomicIntegerFieldUpdater<Simple> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Simple.class, "age");
        Simple simple = new Simple();
        atomicIntegerFieldUpdater.set(simple, 100);

        boolean compareAndSet = atomicIntegerFieldUpdater.compareAndSet(simple, 100, 200);
        assertTrue(compareAndSet);
        assertEquals(atomicIntegerFieldUpdater.get(simple), 200);
    }

}
