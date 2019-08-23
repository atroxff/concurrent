package com.ff.concurrent.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-15 14:34:09
 */
public class LockCondition implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();
    @Override
    public void run() {
        try {
            lock.lockInterruptibly();
            condition.await();
            System.out.println("thread is going on");
        } catch (Exception e) {

        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockCondition lockCondition = new LockCondition();
        Thread t = new Thread(lockCondition);
        t.start();
        Thread.sleep(2000);
        //通知线程继续执行
        lock.lock();
        condition.signal();
        lock.unlock();
    }

}
