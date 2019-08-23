package com.ff.concurrent.threads;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @description:ThreadLocal   ？？？为什么使用了ThreadLocal耗时反而变长？
 * @auther: hefeng
 * @creatTime: 2019-8-16 13:29:53
 */
public class ThreadLocalDemo {

    public static final int GEN_COUNT = 10000000;
    public static final int THREAD_COUNT = 4;
    static ExecutorService exe = Executors.newFixedThreadPool(THREAD_COUNT);
    public static Random rnd = new Random(123);

    //ThreadLocal封装的Random
    public static ThreadLocal<Random> tRnd = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random(123);
        }
    };

    //随机任务
    public static class RndTask implements Callable<Long> {

        private int mode = 0;//0表示多线程共享一个random，1表示各独占一个random

        public RndTask(int mode) {
            this.mode = mode;
        }

        public Random getRandom(){
            if (mode == 0) {
                return rnd;
            } else if (mode == 1) {
                return tRnd.get();
            }else{
                return null;
            }
        }

        @Override
        public Long call() throws Exception {
            long start = System.currentTimeMillis();
            for (int i = 0; i < GEN_COUNT; i++) {
                getRandom();
            }
            long end = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "spend: " + (end - start)+"ms");
            return end - start;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Long>[] futs = new Future[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            futs[i] = exe.submit(new RndTask(0));
        }
        long totaltime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totaltime += futs[i].get();
        }
        System.out.println("多线程访问一个Random实例耗时：" + totaltime + "ms");

        for (int i = 0; i < THREAD_COUNT; i++) {
            futs[i] = exe.submit(new RndTask(1));
        }
        totaltime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totaltime += futs[i].get();
        }
        System.out.println("使用ThreadLocal包装Random实例耗时：" + totaltime + "ms");
        exe.shutdown();
    }


}
