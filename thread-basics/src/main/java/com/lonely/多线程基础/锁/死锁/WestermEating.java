package com.lonely.多线程基础.锁.死锁;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @auther: 15072
 * @date: 2020/2/15 12:02
 * Description: 西方人吃饭案例，两个人共用一套餐具吃饭，但是吃法要求一套餐具，即必须一个人吃完后另一个人再吃
 */
public class WestermEating {

    public static void main(String[] args) {
        TableWare tableWare = new TableWare();

        new Thread(new TakeKnifeFirstThread(tableWare),"A先生").start();
        new Thread(new TakeForckFirstThread(tableWare),"B先生").start();


    }


    private static class TakeKnifeFirstThread implements Runnable {

        private TableWare tableWare;

        public TakeKnifeFirstThread(TableWare tableWare) {
            this.tableWare = tableWare;
        }

        @Override
        public void run() {
            //先拿刀，后拿叉
            synchronized (tableWare.getKnife()) {
                System.out.println(MessageFormat.format("{0}拿到了刀，准备去拿叉", Thread.currentThread().getName()));

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (tableWare.getFork()) {
                    System.out.println(MessageFormat.format("{0}又拿到了叉，可以吃饭了", Thread.currentThread().getName()));
                }
            }
            System.out.println(MessageFormat.format("{0}吃完了", Thread.currentThread().getName()));
        }
    }

    private static class TakeForckFirstThread implements Runnable {

        private TableWare tableWare;

        public TakeForckFirstThread(TableWare tableWare) {
            this.tableWare = tableWare;
        }

        @Override
        public void run() {

            synchronized (tableWare.getFork()) {
                System.out.println(MessageFormat.format("{0}拿到了叉，准备去拿刀", Thread.currentThread().getName()));

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (tableWare.getKnife()) {
                    System.out.println(MessageFormat.format("{0}又拿到了刀，可以吃饭了", Thread.currentThread().getName()));
                }

            }

            System.out.println(MessageFormat.format("{0}吃完了", Thread.currentThread().getName()));

        }
    }


    /**
     * 餐具类
     */
    private static class TableWare {

        /**
         * 刀
         */
        private final String knife = "刀";

        /**
         * 叉
         */
        private final String fork = "叉";

        public String getKnife() {
            return knife;
        }

        public String getFork() {
            return fork;
        }
    }

}


