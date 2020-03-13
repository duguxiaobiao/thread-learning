package com.lonely.juc.同步容器.队列;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.sun.jdi.ArrayReference;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/3/10 14:17
 * Description: 有界的可阻塞队列，在创建的时候指定队列长度，一旦指定不能再修改
 */
public class ArrayBlockingQueueTest {


    /**
     * 测试 add()方法，该方法特点： 添加元素，如果此时队列容量未满，则添加成功，返回true，否则抛出 IllegalStateException 异常
     */
    @Test(expected = IllegalStateException.class)
    public void testAdd() {
        //创建一个容量为2的队列
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        assertThat(arrayBlockingQueue.add("a"), equalTo(true));
        assertThat(arrayBlockingQueue.add("a"), equalTo(true));

        //这里再添加会超过容量抛出异常
        assertThat(arrayBlockingQueue.add("a"), equalTo(true));
        fail("有异常");

    }

    /**
     * 测试 offer()添加元素，与add()不同的是，add()方法容量满了会抛出异常，而offer()则会返回false，不会抛出异常
     * 查看源码，也可以看出 add()方法底层也是调用了 offer()方法
     */
    @Test
    public void testOffer() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        assertThat(arrayBlockingQueue.offer("a"), equalTo(true));
        assertThat(arrayBlockingQueue.offer("a"), equalTo(true));

        //这里容量已经满了，再添加则返回false
        assertThat(arrayBlockingQueue.offer("a"), equalTo(false));
    }

    /**
     * 测试 put()方法,跟add()方法不同的是，如果容量满了，则进入阻塞状态
     *
     * @throws InterruptedException
     */
    @Test
    public void testPut() throws InterruptedException {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        arrayBlockingQueue.put("a");
        arrayBlockingQueue.put("b");

        //这里另启一个线程，用来打断其阻塞状态
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(arrayBlockingQueue::take, 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //如果容器满了，则会阻塞住
        arrayBlockingQueue.put("c");
        assertEquals(arrayBlockingQueue.size(), 2);
        System.out.println("over");
    }

    /**
     * peek()方法，返回队列头的数据，但是不移除数据，只是为了查看使用
     */
    @Test
    public void testPeek() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        arrayBlockingQueue.add("a");
        arrayBlockingQueue.add("b");

        //peek()，返回队列头的数据，但是不移除数据，只是查看使用
        assertThat(arrayBlockingQueue.peek(), equalTo("a"));
        assertThat(arrayBlockingQueue.peek(), equalTo("a"));
    }

    /**
     * 测试 poll()方法，弹出队列头数据，如果队列中没有数据，则返回null
     */
    @Test
    public void testPoll() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        arrayBlockingQueue.add("a");
        arrayBlockingQueue.add("b");

        assertThat(arrayBlockingQueue.poll(), equalTo("a"));
        assertThat(arrayBlockingQueue.poll(), equalTo("b"));

        //此时，队列中已经没有了数据，再调用poll(),则返回null
        assertThat(arrayBlockingQueue.poll(), nullValue());
    }

    /**
     * 测试 remove()方法，跟add()方法一样，remove()方法底层同样调用了 poll()方法，区别就是 remove()方法会抛出异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemove() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        arrayBlockingQueue.add("a");
        arrayBlockingQueue.add("b");

        assertThat(arrayBlockingQueue.remove(), equalTo("a"));
        assertThat(arrayBlockingQueue.remove(), equalTo("b"));

        //如果队列已经空了，此时调用  remove()方法会抛出异常 NoSuchElementException
        assertThat(arrayBlockingQueue.remove(), nullValue());
    }

    /**
     * 测试 take()方法,如果容器空了，则会阻塞
     *
     * @throws InterruptedException
     */
    @Test
    public void testTake() throws InterruptedException {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        arrayBlockingQueue.add("a");
        arrayBlockingQueue.add("b");

        assertThat(arrayBlockingQueue.take(), equalTo("a"));
        assertThat(arrayBlockingQueue.take(), equalTo("b"));

        //模拟退出阻塞，另起线程加入数据
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> arrayBlockingQueue.add("c"), 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //如果容器空了，则会阻塞
        assertThat(arrayBlockingQueue.take(), equalTo("c"));

    }

    /**
     * 测试 remainingCapacity()方法-----返回此时容器的剩余容量
     */
    @Test
    public void testRemainingCapacity() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        arrayBlockingQueue.add("a");

        //remainingCapacity()返回此时容器的剩余容量
        assertThat(arrayBlockingQueue.remainingCapacity(), equalTo(1));
    }

    /**
     * 测试 drainTo()方法---------将容器中的数据导出到其余容器中
     */
    @Test
    public void testDrainTo() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        arrayBlockingQueue.add("a");
        arrayBlockingQueue.add("b");

        List<String> list = new ArrayList<>();

        //将容器的数据导出到 list中
        arrayBlockingQueue.drainTo(list);
        assertEquals(list.size(), 2);
    }

}
