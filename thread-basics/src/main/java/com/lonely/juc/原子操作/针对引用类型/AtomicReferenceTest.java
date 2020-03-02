package com.lonely.juc.原子操作.针对引用类型;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * @auther: 15072
 * @date: 2020/2/29 12:21
 * Description: 关于引用类型的原子操作
 */
public class AtomicReferenceTest {

    /**
     * 测试 创建，获取，和重新设值场景
     */
    @Test
    public void testCreateAndGetAndSet() {

        //默认构造，默认值为null
        AtomicReference<String> atomicReference = new AtomicReference<>();
        assertNull(atomicReference.get());

        //有参构造
        atomicReference = new AtomicReference<>("Hello");
        assertEquals(atomicReference.get(), "Hello");

        //set设置新值
        atomicReference.set("world");
        assertEquals(atomicReference.get(), "world");
    }

    /**
     * 测试 先返回，后操作的一系列api
     */
    @Test
    public void testGetAndXX() {

        AtomicReference<String> atomicReference = new AtomicReference<>("hello");

        //测试 先返回当前value的值，然后重新赋值
        String andSet = atomicReference.getAndSet("world");
        assertEquals(andSet, "hello");
        assertEquals(atomicReference.get(), "world");

        //测试 先返回当前值，然后将当前值作为x，执行后面的函数操作，得到最终的值重新设值为this.value
        atomicReference.set("hello");
        String andUpdate = atomicReference.getAndUpdate(x -> x + " world");
        assertEquals(andUpdate, "hello");
        assertEquals(atomicReference.get(), "hello world");

        //测试 先返回当前值，然后将test作为y，当前this.value作为x，进行函数操作后，将最终的值设值给this.value
        atomicReference.set("hello");
        String andAccumulate = atomicReference.getAndAccumulate("test", (x, y) -> x + y);
        assertEquals(andAccumulate, "hello");
        assertEquals(atomicReference.get(), "hellotest");
    }

    /**
     * 测试先操作，后返回值的一系列操作api
     */
    @Test
    public void testXXXAndGet() {

        AtomicReference<String> atomicReference = new AtomicReference<>("hello");

        //测试 先将 this.value作为x，然后执行函数，将最终的结果赋值给this.value,然后将value返回
        String updateAndGet = atomicReference.updateAndGet(x -> x + x);
        assertEquals(updateAndGet, "hellohello");
        assertEquals(atomicReference.get(), "hellohello");

        //测试 先将 world设置给y，然后将当前this.value设置给x，经过函数操作后，将最终结果赋值给this.value,然后将value返回
        atomicReference.set("hello");
        String accumulateAndGet = atomicReference.accumulateAndGet("world", (x, y) -> x + " " + y);
        assertEquals(accumulateAndGet, "hello world");
        assertEquals(atomicReference.get(), "hello world");
    }

    /**
     * 测试 compareAndSet()方法
     */
    @Test
    public void testCompareAndSet() {
        AtomicReference<String> atomicReference = new AtomicReference<>("hello");
        boolean compareAndSet = atomicReference.compareAndSet("hello", "world");
        assertTrue(compareAndSet);
        assertEquals(atomicReference.get(), "world");
    }

}
