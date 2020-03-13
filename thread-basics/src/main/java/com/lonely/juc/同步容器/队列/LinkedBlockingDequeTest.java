package com.lonely.juc.同步容器.队列;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @auther: 15072
 * @date: 2020/3/11 16:20
 * Description: 跟LinkedBlockingQueue类似，只是该类顶层是一个双向链表，LinkedBlockingQueue只能从队尾加数据，对头删数据，
 * 而 LinkedBlockingQueue既可以从队尾加数据，也可以从对头加数据，同样既可以从对头删数据，也可以从队尾删数据，
 * 本例中不在介绍跟 LinkedBlockingQueue类相同的api，介绍一下该类特有的api
 * 1. 针对新增数据的api中，
 * 1.1 add()等同addLast(), 而addFirst()，如果容量满了，则抛出异常，该方法底层使用的还是offer的方法
 * 1.2 offer()等同offerLast(),而offerFirst()，如果容量满了，则返回null
 * 1.3 put()等同putLast(),而putFirst()，如果容量满了，则会进入阻塞状态
 * <p>
 * 2. 针对删除数据的api中
 * 2.1 poll()等同pollFirst(),而pollLast()从队尾删除数据，如果容量为空，则返回null
 * 2.2 remove()等同removeFirst()，而removeLast()从队尾删除数据，如果容量为空，则会抛出异常，底层使用poll()方法
 * 2.3 take()等同takeFirst(),而takeLast()从队尾删除数据，如果容量为空，则会进入阻塞
 */
public class LinkedBlockingDequeTest {

    /**
     * 测试 addFirst()方法，将数据插入到队头,注意该方法没有返回值，如果容量满了，则直接抛出异常
     */
    @Test(expected = IllegalStateException.class)
    public void testAddFirst() {
        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();
        //add()方法等同 addLast(),都是从队尾加数据
        linkedBlockingDeque.add(2);

        //这里使用addFirst()，从对头加数据
        linkedBlockingDeque.addFirst(1);

        //验证使用 addFirst()方法是否将数据加入到队头
        assertThat(linkedBlockingDeque.peek(), equalTo(1));

        linkedBlockingDeque = new LinkedBlockingDeque<>(1);
        linkedBlockingDeque.addFirst(1);
        linkedBlockingDeque.addFirst(2);

    }

    /**
     * 测试 offerFirst(),如果在有界的情况下，容量满了，不会抛出异常，而是返回false
     */
    @Test
    public void testOfferFirst() {
        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();
        //offer()方法等同 offerLast(),都是从队尾加数据
        linkedBlockingDeque.offer(2);

        //这里使用offerFirst()，从对头加数据
        linkedBlockingDeque.offerFirst(1);

        //验证使用 offerFirst()方法是否将数据加入到队头
        assertThat(linkedBlockingDeque.peek(), equalTo(1));

        linkedBlockingDeque = new LinkedBlockingDeque<>(1);
        assertThat(linkedBlockingDeque.offerFirst(1), equalTo(true));
        //此时容量满了，再添加数据则返回false
        assertThat(linkedBlockingDeque.offerFirst(2), equalTo(false));
    }

    /**
     * 测试 putFirst()方法，在有界的情况下，如果容量满了，则会阻塞
     *
     * @throws InterruptedException
     */
    @Test
    public void testPutFirst() throws InterruptedException {
        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();
        //put()方法等同 putLast(),都是从队尾加数据
        linkedBlockingDeque.putFirst(2);

        //这里使用putFirst()，从对头加数据
        linkedBlockingDeque.putFirst(1);

        //验证使用 putFirst()方法是否将数据加入到队头
        assertThat(linkedBlockingDeque.peek(), equalTo(1));

        LinkedBlockingDeque<Integer> boundLinkedBlockingDeque = new LinkedBlockingDeque<>(1);
        boundLinkedBlockingDeque.putFirst(1);

        //另开线程，2s后删除数据
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> boundLinkedBlockingDeque.take(), 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //此时容量满了，再添加数据则会阻塞
        boundLinkedBlockingDeque.putFirst(2);
        System.out.println("over.......");
    }

    /**
     * 测试pollLast()方法，从队尾移除数据，如果容量为空，则返回null
     */
    @Test
    public void testPoolLast() {
        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();
        linkedBlockingDeque.add(1);
        linkedBlockingDeque.add(2);
        linkedBlockingDeque.add(3);

        //从头移除
        assertThat(linkedBlockingDeque.poll(), equalTo(1));
        //从尾巴移除
        assertThat(linkedBlockingDeque.pollLast(), equalTo(3));

        linkedBlockingDeque.pollLast();

        //此时容量为空，再次调用poll，返回false，不会抛出异常
        assertThat(linkedBlockingDeque.pollLast(), nullValue());
    }


    /**
     * 测试removeLast()方法，从队尾移除数据，如果容量为空，则会抛出 NoSuchElementException异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemoveLast() {
        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();
        linkedBlockingDeque.add(1);
        linkedBlockingDeque.add(2);
        linkedBlockingDeque.add(3);

        //从头移除
        assertThat(linkedBlockingDeque.remove(), equalTo(1));
        //从尾巴移除
        assertThat(linkedBlockingDeque.removeLast(), equalTo(3));

        linkedBlockingDeque.removeLast();

        //此时容量为空，再次调用removeLast()则会抛出异常
        assertThat(linkedBlockingDeque.removeLast(), nullValue());
    }

    /**
     * 测试 takeLast()方法，如果容器为空，则进入阻塞，等待消费
     *
     * @throws InterruptedException
     */
    @Test
    public void testTakeLast() throws InterruptedException {
        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();
        linkedBlockingDeque.add(1);
        linkedBlockingDeque.add(2);
        linkedBlockingDeque.add(3);

        //从头移除
        assertThat(linkedBlockingDeque.take(), equalTo(1));
        //从尾巴移除
        assertThat(linkedBlockingDeque.takeLast(), equalTo(3));

        linkedBlockingDeque.takeLast();

        //另起线程添加数据
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            try {
                linkedBlockingDeque.put(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, TimeUnit.SECONDS);

        //此时容量为空，再次调用takeLast()则会进入阻塞
        assertThat(linkedBlockingDeque.takeLast(), equalTo(3));
        System.out.println("over....");
    }

    /**
     * 测试 peekLast()方法，返回队尾数据，如果容量为空，则返回null
     */
    @Test
    public void testPeekLast() {
        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>(2);
        linkedBlockingDeque.add(1);
        linkedBlockingDeque.add(2);

        assertThat(linkedBlockingDeque.peek(), equalTo(1));
        //使用peekLast()返回队尾数据
        assertThat(linkedBlockingDeque.peekLast(), equalTo(2));

        linkedBlockingDeque.clear();
        //此时容量为空，获取队尾数据，返回null
        assertThat(linkedBlockingDeque.peekLast(), nullValue());

    }


}
