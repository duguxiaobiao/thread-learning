package com.lonely.juc.原子操作.针对基础数据类型;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * @auther: 15072
 * @date: 2020/2/28 16:35
 * Description: 针对int类型的原子操作类 api测试
 */
public class AtomicIntegerTest {

    /**
     * 创建 AtomicInteger,获取当前value值，设置value值等操作
     */
    @Test
    public void testCreateAndGetAndSet() {
        //无参构造
        AtomicInteger atomicInteger = new AtomicInteger();
        assertEquals(atomicInteger.get(), 0);

        //有参构造
        atomicInteger = new AtomicInteger(10);
        assertEquals(atomicInteger.get(), 10);

        //测试set方法
        atomicInteger.set(100);
        assertEquals(atomicInteger.get(), 100);
    }

    /**
     * 测试jdk.8新加方法 accumulateAndGet()
     * 什么意思？ accumulateAndGet(x,Function() f); --> 我们将参数x应用于f函数，将当前get()的值作为f函数的第一个参数，x作为第二个参数，动态返回结果值
     */
    @Test
    public void testAccumulateAndGet() {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        //下面这行代码就是 函数f(x,y) = x- y; 函数的x=get()=10,y=first param = 5,结果 = 10-5 =5
        int i = atomicInteger.accumulateAndGet(5, (x, y) -> x - y);
        assertEquals(i, 5);

        //同理还有先返回，在执行操作的方法
        int andAccumulate = atomicInteger.getAndAccumulate(100, (x, y) -> x + y);
        //这里先返回操作前的值为5，然后在执行加操作，最后结果为105
        assertEquals(andAccumulate, 5);
        assertEquals(atomicInteger.get(), 105);
    }


    /**
     * 测试一系列的关于 先加减在返回值，或者先返回值再加减的操作函数,因为很类似，就在一起写了
     * api命名规则：如果是先获取当前value值，在执行操作的 都是以 getAndXXXX()
     * 如果是先执行value操作，在返回value值的，都是以 XXXAndGet()结尾
     */
    @Test
    public void testGetAndUpdate() {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        //测试 i++ 先返回当前i的值，再加1
        int andIncrement = atomicInteger.getAndIncrement();
        assertEquals(andIncrement, 0);
        assertEquals(atomicInteger.get(), 1);

        //测试 先返回i的值，在加指定n
        atomicInteger.set(0);
        int andAdd = atomicInteger.getAndAdd(10);
        assertEquals(andAdd, 0);
        assertEquals(atomicInteger.get(), 10);

        //测试 先获取当前i的值，在设置新的值
        atomicInteger.set(0);
        int andSet = atomicInteger.getAndSet(100);
        assertEquals(andAdd, 0);
        assertEquals(atomicInteger.get(), 100);

        //测试 i--操作
        atomicInteger.set(0);
        int andDecrement = atomicInteger.getAndDecrement();
        assertEquals(andDecrement, 0);
        assertEquals(atomicInteger.get(), -1);

        //测试 先返回value值，然后将value用于后续的函数，将最新的值重新设置到value中
        atomicInteger.set(0);
        int andUpdate = atomicInteger.getAndUpdate(x -> x + 5);
        assertEquals(andUpdate, 0);
        assertEquals(atomicInteger.get(), 5);
    }

    /**
     * 测试先操作值，在返回值的一系列api
     */
    @Test
    public void testUpdateAndGet() {

        // ++i,先加1，在返回
        AtomicInteger atomicInteger = new AtomicInteger();
        int incrementAndGet = atomicInteger.incrementAndGet();
        assertEquals(incrementAndGet, 1);
        assertEquals(atomicInteger.get(), 1);

        // --i; 先减1，在返回
        atomicInteger.set(0);
        int decrementAndGet = atomicInteger.decrementAndGet();
        assertEquals(decrementAndGet, -1);
        assertEquals(atomicInteger.get(), -1);

        // 先将value + x，在返回
        atomicInteger.set(0);
        int addAndGet = atomicInteger.addAndGet(10);
        assertEquals(addAndGet, 10);
        assertEquals(atomicInteger.get(), 10);

        // f(x,y) = x- y; 将当前value的值设置x,第一个参数设置为y，将操作后的最后结果设置为最新的value值，返回
        atomicInteger.set(0);
        int accumulateAndGet = atomicInteger.accumulateAndGet(10, (x, y) -> x - y);
        assertEquals(accumulateAndGet, -10);
        assertEquals(atomicInteger.get(), -10);

        //f(x) = 10,即将当前value的值，设置为10，然后返回
        atomicInteger.set(0);
        int updateAndGet = atomicInteger.updateAndGet(x -> 10);
        assertEquals(updateAndGet, 10);
        assertEquals(atomicInteger.get(), 10);
    }

    /**
     * 测试 compareAndSet(x,y)方法，这是最终进行替换的原则，cas原则，
     * 意思是，如果预期值x == this.get()--实际值，则进行将value设置为y
     */
    @Test
    public void testCompareAndSet(){
        AtomicInteger atomicInteger = new AtomicInteger();
        boolean compareAndSet = atomicInteger.compareAndSet(0, 10);
        assertTrue(compareAndSet);
        assertEquals(atomicInteger.get(),10);
    }
}
