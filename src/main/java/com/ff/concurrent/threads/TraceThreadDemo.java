package com.ff.concurrent.threads;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-16 10:39:36
 */
public class TraceThreadDemo implements Runnable {

    int a, b;

    public TraceThreadDemo(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        double re = a / b;
        System.out.println(re);
    }

//    public static void main(String[] args) {
//        ThreadPoolExecutor pools = new ThreadPoolExecutor(
//                0,
//                Integer.MAX_VALUE,
//                0L,
//                TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>()
//        );
//
//        for (int i = 0; i < 5; i++) {
//            pools.execute(new TraceThreadDemo(100, i));
//        }
//
//    }

    public static void main(String[] args) {
        ThreadPoolExecutor pools = new TraceThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                0L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );

        for (int i = 0; i < 5; i++) {
            pools.execute(new TraceThreadDemo(100, i));
        }

    }
}
