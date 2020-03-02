package com.lonely.juc.原子操作.针对数组类型;

import lombok.AllArgsConstructor;
import lombok.Data;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @auther: 15072
 * @date: 2020/2/29 15:54
 * Description: 基于引用类型数组的原子操作类 测试类
 */
public class AtomicReferenceArrayTest {

    @Data
    @AllArgsConstructor
    class Simple {
        public String name;
    }

    @Test
    public void testCreateAndGetAndSet() {

        //测试 第一个构造
        AtomicReferenceArray<Simple> atomicReferenceArray = new AtomicReferenceArray<>(1);
        Simple simple = atomicReferenceArray.get(0);
        assertNull(simple);

        //测试 第二种构造
        Simple[] simples = {new Simple("xiaoming")};
        atomicReferenceArray = new AtomicReferenceArray<>(simples);
        Simple simple1 = atomicReferenceArray.get(0);
        assertNotNull(simple1);
        assertEquals(simple1.name, "xiaoming");

        //测试 set方法
        atomicReferenceArray.set(0, new Simple("torny"));
        assertEquals(atomicReferenceArray.get(0).name, "torny");

    }

    /**
     * 测试 先返回指定索引的值，然后再操作的一系列api测试
     */
    @Test
    public void testGetAndXx() {
        Simple simple = new Simple("tony");
        Simple[] simples = {simple};
        AtomicReferenceArray<Simple> atomicReferenceArray = new AtomicReferenceArray<>(simples);

        //测试 先返回指定索引i的值，然后在进行重新赋值
        Simple andSet = atomicReferenceArray.getAndSet(0, new Simple("aa"));
        assertEquals(andSet.name, "tony");
        assertEquals(atomicReferenceArray.get(0).name, "aa");

        //测试 先返回指定索引i的值，然后将指定索引i的值设置为x，然后进行后续操作，重新赋值给索引i
        atomicReferenceArray.set(0, simple);
        Simple andUpdate = atomicReferenceArray.getAndUpdate(0, x -> new Simple(x.name + "bb"));
        assertEquals(andUpdate.name, "tony");
        assertEquals(atomicReferenceArray.get(0).name, "tonybb");

        //测试 先返回指定索引i的值，然后将指定索引i的值设置给x，将cc给y，经过后续操作后，将最终的simple赋值给索引i
        atomicReferenceArray.set(0, simple);
        Simple andAccumulate = atomicReferenceArray.getAndAccumulate(0, new Simple("cc"), (x, y) -> new Simple(x.name + y.name));
        assertEquals(andAccumulate.name, "tony");
        assertEquals(atomicReferenceArray.get(0).name, "tonycc");
    }

    /**
     * 测试先操作，然后返回的一系列api测试
     */
    @Test
    public void testXxAndGet() {
        Simple simple = new Simple("aa");
        Simple[] simples = {simple};
        AtomicReferenceArray<Simple> atomicReferenceArray = new AtomicReferenceArray<>(simples);

        //测试 先将指定索引i的值作为x，然后进行函数操作后，将最终的值重新赋值给索引i，且返回
        Simple updateAndGet = atomicReferenceArray.updateAndGet(0, x -> new Simple(x.name + "bb"));
        assertEquals(updateAndGet.name, "aabb");
        assertEquals(atomicReferenceArray.get(0).name, "aabb");

        //测试 先将指定索引i的值作为x，bb所在simple作为y，经过函数操作后，将最终的值赋值给索引i，且返回
        atomicReferenceArray.set(0, simple);
        Simple accumulateAndGet = atomicReferenceArray.accumulateAndGet(0, new Simple("bb"), (x, y) -> new Simple(y.name + x.name));
        assertEquals(accumulateAndGet.name, "bbaa");
        assertEquals(atomicReferenceArray.get(0).name, "bbaa");

    }

    /**
     * 测试 compareAndSet()方法
     */
    @Test
    public void testCompareAndSet() {
        Simple simple = new Simple("aa");
        Simple[] simples = {simple};
        AtomicReferenceArray<Simple> atomicReferenceArray = new AtomicReferenceArray<>(simples);
        //这里故意重新赋值name字段，就是为了证明，这里是进行simple对象地址的比较，跟name值变化无关
        simple.name = "cc";
        boolean compareAndSet = atomicReferenceArray.compareAndSet(0, simple, new Simple("bb"));
        assertTrue(compareAndSet);
        assertEquals(atomicReferenceArray.get(0).name, "bb");

    }

}
