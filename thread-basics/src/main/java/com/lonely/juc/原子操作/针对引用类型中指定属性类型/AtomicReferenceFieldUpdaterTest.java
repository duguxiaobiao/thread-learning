package com.lonely.juc.原子操作.针对引用类型中指定属性类型;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @auther: 15072
 * @date: 2020/2/29 21:02
 * Description: 针对引用类型中的某一个引用类型类型变量（不能是private，且必须是 volatile修饰）
 */
public class AtomicReferenceFieldUpdaterTest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    class Simple {
        public volatile String name;
    }

    /**
     * 测试 创建，获取，和设置api
     */
    @Test
    public void testCreateAndSetAndGet() {

        //测试 创建
        AtomicReferenceFieldUpdater<Simple, String> atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(Simple.class, String.class, "name");

        //测试 set
        Simple simple = new Simple();
        atomicReferenceFieldUpdater.set(simple, "小明");

        //测试 get方法
        String name = atomicReferenceFieldUpdater.get(simple);
        assertEquals(name, "小明");
    }

    /**
     * 测试 先返回，再操作的一系列api
     */
    @Test
    public void testGetAndXx() {
        AtomicReferenceFieldUpdater<Simple, String> atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(Simple.class, String.class, "name");
        Simple simple = new Simple();
        atomicReferenceFieldUpdater.set(simple, "小明");

        //测试 先获取指定对象的值，然后赋予新值
        String andSet = atomicReferenceFieldUpdater.getAndSet(simple, "小兰");
        assertEquals(andSet, "小明");
        assertEquals(atomicReferenceFieldUpdater.get(simple), "小兰");

        //测试 先获取指定对象的值，然后将当前对象的值作为x，进行后续函数的操作，将最终的结果重新赋值给对象
        atomicReferenceFieldUpdater.set(simple, "小明");
        String andUpdate = atomicReferenceFieldUpdater.getAndUpdate(simple, x -> x + "小兰");
        assertEquals(andUpdate, "小明");
        assertEquals(atomicReferenceFieldUpdater.get(simple), "小明小兰");

        //测试 先获取指定对象的值，然后将指定对象的值作为x，小兰作为y，进行函数操作后，将最终的结果再重新赋予对象
        atomicReferenceFieldUpdater.set(simple, "小明");
        String andAccumulate = atomicReferenceFieldUpdater.getAndAccumulate(simple, "小兰", (x, y) -> x + y);
        assertEquals(andAccumulate, "小明");
        assertEquals(atomicReferenceFieldUpdater.get(simple), "小明小兰");
    }

    /**
     * 测试 先操作，后返回的一系列api操作
     */
    @Test
    public void testXxAndGet() {
        AtomicReferenceFieldUpdater<Simple, String> atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(Simple.class, String.class, "name");
        Simple simple = new Simple();
        atomicReferenceFieldUpdater.set(simple, "小明");

        //测试 先将指定对象的值作为x，执行后续函数操作后，重新赋给对象，且返回
        String updateAndGet = atomicReferenceFieldUpdater.updateAndGet(simple, x -> x + "小花");
        assertEquals(updateAndGet, "小明小花");
        assertEquals(atomicReferenceFieldUpdater.get(simple), "小明小花");

        //测试 先将指定对象的值作为x，小花作为y，然后执行后续函数操作，将最终的结果重新赋给指定对象，且返回
        atomicReferenceFieldUpdater.set(simple, "小明");
        String accumulateAndGet = atomicReferenceFieldUpdater.accumulateAndGet(simple, "小花", (x, y) -> x + y);
        assertEquals(accumulateAndGet, "小明小花");
        assertEquals(atomicReferenceFieldUpdater.get(simple), "小明小花");
    }

    /**
     * 测试 compareAndSet()方法
     */
    @Test
    public void testCompareAndSet() {
        AtomicReferenceFieldUpdater<Simple, String> atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(Simple.class, String.class, "name");
        Simple simple = new Simple();
        atomicReferenceFieldUpdater.set(simple, "小明");

        boolean compareAndSet = atomicReferenceFieldUpdater.compareAndSet(simple, "小明", "小花");
        assertTrue(compareAndSet);
        assertEquals(atomicReferenceFieldUpdater.get(simple), "小花");
    }

}
