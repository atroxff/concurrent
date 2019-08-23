package com.ff.concurrent.threads;

import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.*;

public class ThreadTest {


    public static void main(String[] args) {
        //new Thread对象
//        Thread t1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("thread:"+1);
//            }
//        });
//        t1.start();

        //通过线程池创建
        //fixedThreadPool 固定线程数的线程池
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
//        for(int i=0;i<=5;i++) {
//            final int task = i;
//            fixedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    for(int j=0;j<=5;j++) {
//                        System.out.println(Thread.currentThread().getName()+" "+"task:"+task+" times:"+j);
//                    }
//                }
//            });
//
//
//        }
//        fixedThreadPool.shutdown();

        //cachedThreadPool 可缓存线程池
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//        for(int i=1;i<=100;i++) {
//            //先创建100线程，由于有线程结束，可以先回收利用，不够使用时再创建新线程
//            //同一个线程执行完一个任务以后可以回收继续执行下一个任务
//            final int task = i;
//            cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    for(int j=1;j<=5;j++) {
//                        System.out.println(Thread.currentThread().getName()+" "+"task:"+task+" times:"+j);
//                    }
//                }
//            });
//        }
//        cachedThreadPool.shutdown();

        //singleThreadExecutor 单线程化线程池  以唯一工作线程来执行任务，保证任务按照指定顺序执行
//        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//        for(int i=0;i<10;i++) {
//            final int task = i;
//            singleThreadExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        System.out.println(Thread.currentThread().getName()+" "+"task:"+task);
//                        Thread.sleep(200);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//        singleThreadExecutor.shutdown();


        //scheduledThreadPool  定时线程池，支持定时及周期性任务执行
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        System.out.println("scheduledThreadPool creat finished,wait 5 seconds");
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("delay 5s");

            }
        }, 5, TimeUnit.SECONDS);
        scheduledThreadPool.shutdown();


    }

}
