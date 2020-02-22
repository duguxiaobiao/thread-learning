    package com.lonely.多线程基础.捕捉线程异常.线程中异常演示案例;

    /**
     * @auther: 15072
     * @date: 2020/2/16 15:23
     * Description: 演示一个简单的 线程中出现异常的场景
     */
    public class SimpleErrorThread {


        public static void main(String[] args) {
            try {
                new Thread(()->{
                    int i = 0;
                    while (true){
                        i++;
                        if(i>20){
                            throw new RuntimeException("线程中手动抛出异常");
                        }
                    }
                }).start();
            } catch (Exception e) {
                System.out.println("接收到线程中的异常");
                e.printStackTrace();
            }
        }



    }
