package com.ff.concurrent.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-15 13:59:11
 */
public class TimeLock implements Runnable{
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;

    public TimeLock(int lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        if (lock == 1) {//lock1
            while (true) {
                if (lock1.tryLock()) {
                    try{
                        try{
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                        }
                        if (lock2.tryLock()){
                            try{
                                System.out.println(Thread.currentThread().getId()+":done");
                                return;
                            }finally {
                                lock2.unlock();
                            }
                        }
                    }finally {
                        lock1.unlock();
                    }
                }
            }
        }else{//lock2
            while (true) {
                if (lock2.tryLock()) {
                    try{
                        try{
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                        }
                        if (lock1.tryLock()){
                            try{
                                System.out.println(Thread.currentThread().getId()+":done");
                                return;
                            }finally {
                                lock1.unlock();
                            }
                        }
                    }finally {
                        lock2.unlock();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        TimeLock r1 = new TimeLock(1);
        TimeLock r2 = new TimeLock(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }
}
