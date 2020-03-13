package com.lonely.juc.同步容器.队列;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/3/11 14:58
 * Description: 底层使用链表实现的队列，可选的有界队列，既可以是有边界，也可以是无边界的，取决于new的时候的入参
 * 总结一下:
 * 1. 针对新增数据的3个api，无界的情况就不说了，不会出现容量慢的情况，这里针对有界的情况
 * 1.1 offer()方法，如果，超过容量了，返回false
 * 1.2 add()方法，底层使用offer()方法，如果容量满了，则抛出异常
 * 1.3 put()方法，如果容量满了，则会阻塞
 * <p>
 * 2. 针对弹出队列头数据的3个api，这里不区分有界和无界，都是一样
 * 2.1 poll()方法，如果队列为空，则返回null
 * 2.1 remove()方法，如果队列为空，则抛出异常
 * 2.3 take()方法，如果队列为空，则会阻塞
 * 3. 针对查看队列头元素的2个api
 * 3.1 peek()方法，如果队列为空，则返回null
 * 3.2 element()方法，底层使用peek()方法，如果队列为空，则抛出异常
 */
public class LinkedBlockingQueueTest {


    /**
     * 测试offer()方法，添加非空元素，如果是无边界的，则offer()添加元素，返回true，如果是有边界的，如果容器满了，则返回false
     */
    @Test
    public void testOffer() {

        //创建一个无界的，其实也是有最大值的，即Integer.MAX_VALUE
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        assertThat(linkedBlockingQueue.offer(1), equalTo(true));
        assertThat(linkedBlockingQueue.offer(2), equalTo(true));
        assertThat(linkedBlockingQueue.size(), equalTo(2));

        //创建一个有界的队列，使用offer()方法，如果队列满了，则返回false，不抛出异常
        linkedBlockingQueue = new LinkedBlockingQueue<>(2);
        assertThat(linkedBlockingQueue.offer(1), equalTo(true));
        assertThat(linkedBlockingQueue.offer(2), equalTo(true));
        assertThat(linkedBlockingQueue.offer(3), equalTo(false));
    }

    /**
     * 测试add()方法
     */
    @Test(expected = IllegalStateException.class)
    public void testAdd() {
        //创建无界队列
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueue.add(1);
        linkedBlockingQueue.add(2);
        assertThat(linkedBlockingQueue.size(), equalTo(2));

        //创建有界队列
        linkedBlockingQueue = new LinkedBlockingQueue<>(2);
        linkedBlockingQueue.add(1);
        linkedBlockingQueue.add(2);
        //此时队列满了，会抛出 IllegalStateException异常
        assertThat(linkedBlockingQueue.add(3), equalTo(false));
    }

    /**
     * 测试put()方法,如果是无界的，则不会阻塞，如果是有界的，容量满了，就会阻塞住，等待消费
     *
     * @throws InterruptedException
     */
    @Test
    public void testPut() throws InterruptedException {

        //创建无界队列
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueue.put(1);
        linkedBlockingQueue.put(2);
        assertThat(linkedBlockingQueue.size(), equalTo(2));

        LinkedBlockingQueue<Integer> boundLinkedBlockingQueue = new LinkedBlockingQueue<>(2);
        boundLinkedBlockingQueue.put(1);
        boundLinkedBlockingQueue.put(2);

        //让其2s后消费一个数据
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            try {
                assertThat(boundLinkedBlockingQueue.take(), equalTo(1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //此时队列容量满了，则会阻塞，另起线程先消费一个，让其放入成功
        boundLinkedBlockingQueue.put(3);
        System.out.println("over....");
    }

    /**
     * 测试peek()方法
     */
    @Test
    public void testPeek() {
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        //不插入数据，直接peek()返回null,这里不区分有界还是无界的
        assertThat(linkedBlockingQueue.peek(), nullValue());

        linkedBlockingQueue = new LinkedBlockingQueue<>(1);
        assertThat(linkedBlockingQueue.peek(), nullValue());
    }

    /**
     * element()方法，底层使用peek()方法，如果容量为空，则element()方法抛出异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testElement() {

        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueue.add(1);
        linkedBlockingQueue.add(2);
        assertThat(linkedBlockingQueue.element(), equalTo(1));

        linkedBlockingQueue = new LinkedBlockingQueue<>(1);
        assertThat(linkedBlockingQueue.element(), nullValue());

    }

}
