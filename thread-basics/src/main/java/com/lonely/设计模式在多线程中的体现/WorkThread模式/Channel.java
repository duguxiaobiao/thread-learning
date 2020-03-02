package com.lonely.设计模式在多线程中的体现.WorkThread模式;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther: 15072
 * @date: 2020/2/25 16:12
 * Description: 传送带
 */
public class Channel {

    public static final int MAX_SIZE = 100;

    private LinkedList<Goods> goodsList;

    private List<WorkerThread> workThreads;


    public Channel(int workers) {
        goodsList = new LinkedList<>();
        workThreads = new ArrayList<>();
        this.init(workers);
    }

    private void init(int workers) {
        for (int i = 1; i <= workers; i++) {
            workThreads.add(new WorkerThread("组装工人" + i, this));
        }
    }

    /**
     * 开始工作
     */
    public void startWorker() {
        this.workThreads.forEach(WorkerThread::start);
    }


    /**
     * 搬运工将商品放入到传送带
     *
     * @param goods
     */
    public void put(Goods goods) {
        synchronized (goodsList) {
            while (goodsList.size() >= MAX_SIZE) {
                //满了，等待
                try {
                    goodsList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //放入到队列中
            goodsList.addLast(goods);
            goodsList.notifyAll();
        }
    }

    /**
     * 从传动带上获取商品
     *
     * @return
     */
    public Goods take() {
        synchronized (goodsList) {
            while (goodsList.isEmpty()) {
                //没有商品了，休息
                try {
                    goodsList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //取出商品
            Goods goods = goodsList.removeFirst();
            goodsList.notifyAll();
            return goods;
        }
    }


}
