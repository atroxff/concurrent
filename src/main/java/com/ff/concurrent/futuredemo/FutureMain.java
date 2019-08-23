package com.ff.concurrent.futuredemo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-16 16:22:27
 */
public class FutureMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> future = new FutureTask<String>(new RealData("a"));
        ExecutorService exe = Executors.newFixedThreadPool(1);
        exe.execute(future);
        System.out.println(System.currentTimeMillis()+":请求完毕");
        System.out.println(System.currentTimeMillis() + ":main task done");
        Thread.sleep(5000);
        System.out.println(System.currentTimeMillis()+":main wait finish");
        System.out.println(future.get());

    }
}
