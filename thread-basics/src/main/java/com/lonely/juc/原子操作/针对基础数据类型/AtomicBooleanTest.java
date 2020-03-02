package com.lonely.juc.原子操作.针对基础数据类型;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * @auther: 15072
 * @date: 2020/2/28 19:59
 * Description: 针对boolean类型操作原子类
 */
public class AtomicBooleanTest {

    /**
     * 测试 如何创建，获取，设值
     */
    @Test
    public void testCreateAndGetAndSet() {
        //初始化和get,默认为false----底层还是使用 volatile int value, 默认value = 0 -- false
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        assertFalse(atomicBoolean.get());

        //测试有参构造
        atomicBoolean = new AtomicBoolean(false);
        assertFalse(atomicBoolean.get());

        //set方法
        atomicBoolean.set(false);
        assertFalse(atomicBoolean.get());
    }

    /**
     * 测试 getAndSet()方法，先获取，然后进行设值，使用cas机制
     */
    @Test
    public void testGetAndSet() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        boolean andSet = atomicBoolean.getAndSet(true);
        assertFalse(andSet);
        assertTrue(atomicBoolean.get());
    }

    /**
     * 测试 compareAndSet()方法
     */
    @Test
    public void testCompareAndSet() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        //比较后，进行设值
        boolean compareAndSet = atomicBoolean.compareAndSet(false, true);
        //是否更新成功
        assertTrue(compareAndSet);
        //更新后的值为true
        assertTrue(atomicBoolean.get());
    }

}
