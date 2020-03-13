package com.lonely.juc.同步容器.队列;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @auther: 15072
 * @date: 2020/3/12 10:22
 * Description: 测试 DelayQueue 的api,该类的泛型需要实现 Delayed接口，实现根据时间来判断是否过期，同时Delayed接口实现了Comparable实现根据
 * 时间来先后排序，该类注重删除数据时用到了过期特性，而新增数据则是普通的队列性质，添加到队列中，因为底层使用了PriorityBlockingQueue，所以
 * 会有排序效果
 * 而在删除数据的api中，offer()和remove()是即时的，如果队列为空或者没有过期数据，要么返回null，要么抛出异常，而take()方法则会阻塞住，
 * 等待取出过期数据
 */
public class DelayQueueTest {

    static class SimpleDelayed implements Delayed {


        /**
         * 计算后的超时时间 以纳秒计算,因为 DelayQueue的源码中都是以纳秒计算
         */
        private long time;

        /**
         * 延时
         */
        private long delayed;

        /**
         * 时间单位
         */
        private TimeUnit timeUnit;

        /**
         * 构造方法
         *
         * @param delayed  指定延迟时间
         * @param timeUnit 单位
         */
        SimpleDelayed(long delayed, TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            this.delayed = delayed;
            this.time = TimeUnit.NANOSECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS) + TimeUnit.NANOSECONDS.convert(delayed, timeUnit);
        }

        /**
         * 比较是否过期 小于等于0 表示过期
         *
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return this.time - unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        /**
         * 根据时间排序，从小到大
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            SimpleDelayed simpleDelayed = (SimpleDelayed) o;

            if (this.getTime() < simpleDelayed.getTime()) {
                return -1;
            } else if (this.getTime() > simpleDelayed.getTime()) {
                return 1;
            } else {
                return 0;
            }
        }

        public long getDelayed() {
            return delayed;
        }

        public long getTime() {
            return time;
        }
    }

    /**
     * 测试offer()方法,该方法底层使用 PriorityBlockingQueue的offer()方法，因为是无界的，所以一般返回true，除非内存溢出
     */
    @Test
    public void testOffer() {
        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        assertThat(delayeds.offer(new SimpleDelayed(1, TimeUnit.SECONDS)), equalTo(true));
        assertThat(delayeds.size(), equalTo(1));
    }

    /**
     * 测试 add()方法，底层就是使用 offer()方法，两个方法没区别
     */
    @Test
    public void testAdd() {
        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        assertThat(delayeds.add(new SimpleDelayed(1, TimeUnit.SECONDS)), equalTo(true));
        assertThat(delayeds.size(), equalTo(1));
    }

    /**
     * 测试put()方法，该方法底层直接使用offer(),两者没区别
     */
    @Test
    public void testPut() {
        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        delayeds.put(new SimpleDelayed(1, TimeUnit.SECONDS));
        assertThat(delayeds.size(), equalTo(1));
    }

    /**
     * 测试poll()方法 如果队列头不是过期的元素，则返回null，否则返回队列头元素
     *
     * @throws InterruptedException
     */
    @Test
    public void testPoll() throws InterruptedException {
        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        assertThat(delayeds.add(new SimpleDelayed(2, TimeUnit.SECONDS)), equalTo(true));
        assertThat(delayeds.add(new SimpleDelayed(1, TimeUnit.SECONDS)), equalTo(true));
        assertThat(delayeds.size(), equalTo(2));

        //使用poll()移除元素,如果队列头不是过期的元素，则返回null，否则返回队列头元素
        //此时还没有过期，应该返回null
        assertThat(delayeds.poll(), nullValue());

        //等待1s
        TimeUnit.SECONDS.sleep(1);
        assertThat(delayeds.poll().getDelayed(), equalTo(1L));
    }


    /**
     * 测试 remove()方法， 该方法底层使用了poll()方法，如果容器为空，或者队列头没有过期，则抛出异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemove() {

        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        //此时容量为空，会抛出异常
        //delayeds.remove();

        delayeds.add(new SimpleDelayed(2, TimeUnit.SECONDS));
        //此时元素还没过期，使用remove()，会抛出异常,如果休眠2s，则可以了
        delayeds.remove();
    }

    /**
     * 测试take()方法,如果容量为空或者队列头还没有过期，则会一直等待
     *
     * @throws InterruptedException
     */
    @Test
    public void testTake() throws InterruptedException {
        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        //此时没有数据，会一直等待
        //delayeds.take();

        delayeds.add(new SimpleDelayed(2, TimeUnit.SECONDS));

        //此时添加了一个元素，但是2s后过期，所以这里会等待2s后才能拿到数据
        SimpleDelayed take = delayeds.take();
        assertThat(take.getDelayed(), equalTo(2L));
        System.out.println("over....");
    }

    /**
     * 测试  peek()方法，该方法底层直接使用 PriorityBlockingQueue的peek()方法，如果容器为空，则返回null，否则直接返回队头数据，不会在意是否过期
     */
    @Test
    public void testPeek() {
        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        //此时没有数据，会返回null
        assertThat(delayeds.peek(), nullValue());

        delayeds.add(new SimpleDelayed(2, TimeUnit.SECONDS));

        //这里不会在意是否过期，立即返回队头数据
        assertThat(delayeds.peek().getDelayed(), equalTo(2L));
    }


    /**
     * 测试element()方法， 该方法底层使用 peek()方法，区别在于如果容器为空，则直接抛出异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testElement() {
        DelayQueue<SimpleDelayed> delayeds = new DelayQueue<>();
        //此时没有数据，会抛出异常
        delayeds.element();

        delayeds.add(new SimpleDelayed(2, TimeUnit.SECONDS));

        //这里不会在意是否过期，立即返回队头数据
        assertThat(delayeds.peek().getDelayed(), equalTo(2L));
    }

}
