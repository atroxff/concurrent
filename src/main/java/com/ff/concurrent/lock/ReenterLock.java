package com.ff.concurrent.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-15 13:09:15
 */
public class ReenterLock implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 10; j++) {
            lock.lock();
            //lock.lock();//可以多次加锁，相应地要解锁
            try{
                i++;
            }finally {
                lock.unlock();
                //lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLock reenterLock = new ReenterLock();
        Thread t1 = new Thread(reenterLock);
        Thread t2 = new Thread(reenterLock);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
