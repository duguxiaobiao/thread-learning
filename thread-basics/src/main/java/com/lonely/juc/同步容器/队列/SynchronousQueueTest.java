package com.lonely.juc.同步容器.队列;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/3/11 18:44
 * Description: 测试 SynchronousQueue的api，该类是一个有界的阻塞容器，该类有一个特点，虽然是容器，但是如果没有一个等待消费的线程，则不能
 * 放入数据，应用于数据交换场景，想象一下，如果有两个人需要交易，需要两者面对面交易，如果有一方没到，则该交易就无法成功。
 * <p>
 * 总结一下：
 * 该类的 新增和移除方法是匹配使用的，不能单独使用，而在新增的api中，add方法和offer方法是即时的(两者区别就是offer()返回false，而add()抛出异常)，而put()方法是阻塞的
 * 在移除的api中，remove方法和poll方法也是即时的(区别在于poll()方法是返回null，而remove()直接抛出异常)，而take()方法是阻塞的
 * 而获取对头的2个api，在该类中是无法使用的，peek()方法是直接返回null，而element()方法是直接抛出异常
 */
public class SynchronousQueueTest {

    /**
     * offer()方法，如果没有一个线程在等待，则直接返回false，不抛出异常
     *
     * @throws InterruptedException
     */
    @Test
    public void testOffer() throws InterruptedException {
        //入参可以传入boolean类型，如果为true，则设置为公平锁实现
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(synchronousQueue::take, 0, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //此时因为没有等待消费的线程，无法加入其中，会抛出异常,因此在前面添加了消费线程
        //offer()方法是没有等待时间的，因此如果执行到offer()的时候如果没有线程等着，则直接抛出异常,因此休眠1s，使其消费者先执行
        TimeUnit.SECONDS.sleep(1);
        assertThat(synchronousQueue.offer(1), equalTo(true));
        System.out.println("over...");
    }


    /**
     * 测试 add()方法，底层使用 offer()区别，区别就是如果没有线程等待，则直接抛出异常
     *
     * @throws InterruptedException
     */
    public void testAdd() throws InterruptedException {
        //入参可以传入boolean类型，如果为true，则设置为公平锁实现
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(synchronousQueue::take, 0, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //此时因为没有等待消费的线程，无法加入其中，会抛出异常,因此在前面添加了消费线程
        //add()方法是没有等待时间的，因此如果执行到add()的时候如果没有线程等着，则直接抛出异常,因此休眠1s，使其消费者先执行
        TimeUnit.SECONDS.sleep(1);
        assertThat(synchronousQueue.add(1), equalTo(true));
    }


    /**
     * 测试 put()方法，如果此时没有线程等待消费，则会一直阻塞
     *
     * @throws InterruptedException
     */
    @Test
    public void testPut() throws InterruptedException {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(synchronousQueue::take, 0, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //此时因为没有等待消费的线程，无法加入其中，会进入阻塞,因此在前面添加了消费线程，让其完成交易
        //put()方法是会阻塞的，会一直等待消费者消费
        TimeUnit.SECONDS.sleep(1);
        synchronousQueue.put(1);
        System.out.println("over");
    }

    /**
     * 测试poll()方法,如果没有线程放入数据，则直接返回null，不会抛出异常
     */
    @Test
    public void testPoll() {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        //poll()是不会阻塞的，如果没有线程放入数据，则直接返回null
        assertThat(synchronousQueue.poll(), equalTo(null));
    }


    /**
     * 测试 remove()方法，底层使用poll()方法，区别在于如果没有线程放入数据，则直接抛出异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemove() {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        //remove()是不会阻塞的，如果没有线程放入数据，则会抛出 NoSuchElementException 异常
        assertThat(synchronousQueue.remove(), equalTo(null));
    }

    /**
     * 测试take()方法,该方法如果此时没有线程放入数据，则会进入阻塞中
     */
    @Test
    public void testTake() throws InterruptedException {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            try {
                synchronousQueue.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        //take()方法如果此时没有线程放入数据，则会进入阻塞，因此在上面添加一个线程执行put()操作，就会取消阻塞了
        synchronousQueue.take();
        System.out.println("over...");
    }

    /**
     * 测试 peek()方法，该方法直接返回null，因为该类本就没有容量，不是暂存空间类型
     */
    @Test
    public void testPeek() {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        assertThat(synchronousQueue.peek(), equalTo(null));
    }

    /**
     * 测试element()方法，底层使用 peek()方法，因为peek()方法只会返回null，因此element()方法一定会抛出异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testElement() {
        SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();
        assertThat(synchronousQueue.element(), equalTo(null));
    }

}
