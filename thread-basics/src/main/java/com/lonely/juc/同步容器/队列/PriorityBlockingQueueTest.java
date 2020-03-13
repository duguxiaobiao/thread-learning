package com.lonely.juc.同步容器.队列;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @auther: 15072
 * @date: 2020/3/11 11:05
 * Description: 支持排序的无界不阻塞队列,与PriorityQueue相比，PriorityBlockQueue是线程安全的，用于多线程环境
 * 特点：
 * 针对新增数据的三个api而言(这里添加的元素不能为null)，底层实现都是用offer()方法，所以在该类中，新增数据三个api都是一样的，且无阻塞
 * 针对删除数据的三个api而言，remove()方法底层使用poll()方法，poll()方法如果队列为空，则返回null，而remove()方法则是抛出异常，而task()方法则会阻塞
 * 针对查看队列头元素的两个api而言，element()方法底层使用peek()方法，peek()方法如果队列为空，则返回null，而element()方法如果队列为空，则会抛出异常
 */
public class PriorityBlockingQueueTest {

    /**
     * 测试add()方法，底层就是 offer()方法，所以在该类中，add()==offer(),只会返回true，除非内存溢出
     */
    @Test
    public void testAdd() {
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        //add()方法永远不会抛出false，因为是无边界的，除非内存溢出，因为是无边界的
        assertThat(priorityBlockingQueue.add(1), equalTo(true));
        assertThat(priorityBlockingQueue.add(2), equalTo(true));
        assertThat(priorityBlockingQueue.size(), equalTo(2));
    }

    /**
     * 测试offer(),添加元素，永远返回true，除非内存溢出
     */
    @Test
    public void testOffer() {
        //默认长度为11
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();

        assertThat(priorityBlockingQueue.offer(2), equalTo(true));
        assertThat(priorityBlockingQueue.offer(1), equalTo(true));
        assertThat(priorityBlockingQueue.size(), equalTo(2));
    }

    /**
     * 测试put()方法,底层就是offer(),只是无返回值
     */
    @Test
    public void testPut() {
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.put(2);
        priorityBlockingQueue.put(1);
        assertThat(priorityBlockingQueue.size(), equalTo(2));
    }

    /**
     * 测试poll()方法，将排序后的队列头数据取出来
     */
    @Test
    public void testPoll() {
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.add(2);
        priorityBlockingQueue.add(1);

        //注意添加进去后，会根据自然排序来排序，所以虽然先加入2，然是这里返回的排序后的队列头，所以返回1
        assertThat(priorityBlockingQueue.poll(), equalTo(1));
    }

    /**
     * 测试remove()方法，底层是哦那个poll()方法，区别就是poll()方法如果队列中没有数据会返回null，而remove()则会抛出异常
     */
    @Test
    public void testRemove() {
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.add(2);
        priorityBlockingQueue.add(1);
        priorityBlockingQueue.add(3);
        assertThat(priorityBlockingQueue.remove(), equalTo(1));
    }

    /**
     * 测试take()方法，如果队列为空，则会阻塞住
     *
     * @throws InterruptedException
     */
    @Test
    public void testTake() throws InterruptedException {

        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.add(2);
        priorityBlockingQueue.add(1);

        //take()方法如果队列为空，取不到数据时，会阻塞住
        assertThat(priorityBlockingQueue.take(), equalTo(1));
        assertThat(priorityBlockingQueue.take(), equalTo(2));

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> priorityBlockingQueue.add(3), 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //如果不添加数据，这里会阻塞住，所以另起线程添加数据
        assertThat(priorityBlockingQueue.take(), equalTo(3));
        System.out.println("over");
    }


    /**
     * 测试peek()方法,如果队列为空，则返回null
     */
    @Test
    public void testPeek(){
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.add(2);
        priorityBlockingQueue.add(3);
        priorityBlockingQueue.add(1);
        assertThat(priorityBlockingQueue.peek(),equalTo(1));
        assertThat(priorityBlockingQueue.peek(),equalTo(1));
    }


    /**
     * 测试element()方法，该方法底层还是使用peek()方法，区别就是如果队列为空，则抛出异常，这里故意让队列为空，验证是否会抛出该异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testElement(){
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        assertThat(priorityBlockingQueue.element(),nullValue());
    }
}
