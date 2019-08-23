package com.ff.concurrent.threads;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-15 09:27:54
 */
public class SimpleWaitNotify {
    final static Object obj = new Object();
    public static class T1 extends Thread{
        public void run(){
            while(true){
                synchronized (obj){
                    System.out.println("T1 start:"+System.currentTimeMillis());
                    try {
                        System.out.println("T1 wait:"+System.currentTimeMillis());
                        obj.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("T1 end:"+System.currentTimeMillis());
                }
            }
        }
    }
    public static class T2 extends Thread{
        public void run(){
            while(true){
                synchronized (obj){
                    //在notify以后不是立即执行t1 end,而是继续执行t2直到释放obj监视器
                    System.out.println("T2 start:"+System.currentTimeMillis());
                    System.out.println("notify one:"+System.currentTimeMillis());
                    obj.notify();
                    System.out.println("T2 end:"+System.currentTimeMillis());
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new T1();
        Thread t2 = new T2();
        t1.start();
        t2.start();

    }
}
