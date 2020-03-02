package com.lonely.juc.原子操作.针对引用类型;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @auther: 15072
 * @date: 2020/2/29 17:14
 * Description: 基于引用类型的，解决aba问题的第二种 使用boolean标记法
 */
public class AtomicMarkableReferenceTest {

    /**
     * 测试
     */
    @Test
    public void testCreateAndGet() {

        //测试 构造方法
        AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("aa", true);

        //测试get方法, 只返回value值的方式
        String reference = atomicMarkableReference.getReference();
        assertEquals(reference, "aa");
        assertTrue(atomicMarkableReference.isMarked());

        //同时返回value，且返回标识的方式 --- 传入的数组必须长度1，
        boolean[] bools = new boolean[1];
        String value = atomicMarkableReference.get(bools);
        assertEquals(value, "aa");
        assertTrue(bools[0]);

        //测试 set方法
        atomicMarkableReference.set("bb", false);
        assertEquals(atomicMarkableReference.getReference(), "bb");
        assertFalse(atomicMarkableReference.isMarked());
    }

    /**
     * 测试 attemptMark(),当预期引用 == 当前应用，则可以赋予新标识
     */
    @Test
    public void testAttemptMark() {
        AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("aa", true);
        boolean attemptMark = atomicMarkableReference.attemptMark("aa", false);
        assertTrue(attemptMark);
        assertEquals(atomicMarkableReference.getReference(), "aa");
        assertFalse(atomicMarkableReference.isMarked());
    }

    /**
     * 测试 compareAndSet()
     */
    @Test
    public void testCompareAndSet() {
        AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<>("aa", true);
        atomicMarkableReference.compareAndSet("aa", "bb", true, false);
        assertEquals(atomicMarkableReference.getReference(), "bb");
        assertFalse(atomicMarkableReference.isMarked());
    }

}
