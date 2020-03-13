package com.lonely.juc.同步容器.队列;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @auther: 15072
 * @date: 2020/3/11 19:42
 * Description: TransferQueue类的使用，该类是LinkedBlockingQueue和SynchronousQueue的结合体，因为SynchronousQueue无法保存数据
 * 底层使用链表，也拥有同 SynchronousQueue类似的transfer方法，也是同交易性质
 * <p>
 * 总结一下：
 * 1. 针对新增的4个api来说，offer()和add()以及put()方法的实现是一样的，都是即时响应，添加到队尾，
 * 而transfer()方法则会判断当前是否有等待消费的线程，如果没有，则会阻塞，tryTransfer()方法如果此时有等待消费的直接给与消费，返回true，否则等待
 * <p>
 * 2. 针对删除的3个api来说，offer()和remove()方法不会阻塞，如果容量为空，一个返回null，一个抛出异常，而take()方法，则会阻塞，等待消费
 */
public class TransferQueueTest {

    /**
     * 测试 offer()方法，该方法的效果其实跟普通的队列一样，加入到队列中
     */
    @Test
    public void testOffer() {
        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
        assertThat(transferQueue.offer(1), equalTo(true));
        assertThat(transferQueue.offer(2), equalTo(true));
        assertThat(transferQueue.size(), equalTo(2));
    }

    /**
     * 测试add()方法，该方法效果和offer()一样的
     *
     * @throws InterruptedException
     */
    @Test
    public void testAdd() throws InterruptedException {
        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
        assertThat(transferQueue.add(1), equalTo(true));
        assertThat(transferQueue.size(), equalTo(1));
    }

    /**
     * 测试 put()方法，底层实现同offer()和add(),因为是无界队列，因此直接加入到队列中
     *
     * @throws InterruptedException
     */
    @Test
    public void testPut() throws InterruptedException {
        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
        transferQueue.put(1);
        assertThat(transferQueue.size(), equalTo(1));
    }


    /**
     * 测试 transfer()方法，如果当前没有消费者线程等待数据，则直接将数据插入到队尾并且阻塞住等待消费
     *
     * @throws InterruptedException
     */
    @Test
    public void testTransfer() throws InterruptedException {
        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
        transferQueue.add(1);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                System.out.println(transferQueue.take());
                //assertThat(transferQueue.take(), equalTo(2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
        //scheduledExecutorService.shutdown();

        //此时因为没有消费者线程等待消费数据，所以会一直阻塞，所以上面写一个消费者线程
        transferQueue.transfer(2);
        TimeUnit.SECONDS.sleep(3);
        System.out.println("over....");
        scheduledExecutorService.shutdown();
    }

    /**
     * 测试 tryTransfer()方法，尝试交易数据，如果此时没有消费者线程等待消费，则返回false，否则立马消费，不会阻塞
     *
     * @throws InterruptedException
     */
    @Test
    public void testTryTransfer() throws InterruptedException {

        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();

        //返回是否存在正在等消费的线程
        assertThat(transferQueue.hasWaitingConsumer(), equalTo(false));

        //返回正在等消费的线程的数量
        assertThat(transferQueue.getWaitingConsumerCount(), equalTo(0));

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(transferQueue::take, 0, TimeUnit.SECONDS);

        //tryTransfer()尝试交易，如果有正在等待消费的线程，则立马消费，否则返回false
        //此时没有线程take()，肯定返回false，所以另起线程，消费数据
        TimeUnit.MILLISECONDS.sleep(100);

        //此时已经有线程take了
        assertThat(transferQueue.hasWaitingConsumer(), equalTo(true));
        assertThat(transferQueue.getWaitingConsumerCount(), equalTo(1));

        //尝试返回true
        assertThat(transferQueue.tryTransfer(1), equalTo(true));
        System.out.println("over...");
    }


    /**
     * 测试poll()方法，立马返回队头的值
     *
     * @throws InterruptedException
     */
    @Test
    public void testPoll() throws InterruptedException {
        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
        transferQueue.add(1);

        //Integer poll = transferQueue.poll(2, TimeUnit.SECONDS);
        //System.out.println(poll);

        //poll()立马返回队头的值
        assertThat(transferQueue.poll(), equalTo(1));

        //使用 poll(2,TimeUnit.SECONDS);来配合transfer()
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> transferQueue.poll(2, TimeUnit.SECONDS), 2, TimeUnit.SECONDS);

        transferQueue.transfer(1);
        System.out.println("over...");
    }

    /**
     * 测试 remove()方法
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemove() {

        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();
        transferQueue.add(2);

        //底层使用poll()，如果队列为空，则抛出异常
        assertThat(transferQueue.remove(), equalTo(2));
        assertThat(transferQueue.remove(), nullValue());
    }

    /**
     * 测试take()方法，如果没有线程放入数据，则会一直自旋等待
     *
     * @throws InterruptedException
     */
    @Test
    public void testTake() throws InterruptedException {
        TransferQueue<Integer> transferQueue = new LinkedTransferQueue<>();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> transferQueue.add(1), 2, TimeUnit.SECONDS);

        //如果没有县城放入数据，则会一直等待,所以在前面开启线程放入数据
        assertThat(transferQueue.take(), equalTo(2));
        System.out.println("over...");
    }

}
