package com.ff.concurrent.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-15 15:22:34
 */
public class CyclicBarrierDemo {
    public static class Soldier implements Runnable{
        private String soldier;
        private final CyclicBarrier cyclic;

        public Soldier(String soldierName, CyclicBarrier cyclic) {
            this.soldier = soldierName;
            this.cyclic = cyclic;
        }

        @Override
        public void run() {
            try {
                //等待所有士兵到齐
                cyclic.await();
                doWork();
                //等待所有士兵完成工作
                cyclic.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void doWork() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(soldier+" done");
        }
    }

    public static class BarrierRun implements Runnable {
        boolean flag;
        int N;

        public BarrierRun(boolean flag, int N) {
            this.flag = flag;
            this.N = N;
        }

        @Override
        public void run() {
            if (flag) {
                System.out.println("boss call: soldier done finished");
            }else{
                System.out.println("boss call: soldier come finished");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;
        Thread[] allThreads = new Thread[N];
        boolean flag = false;
        CyclicBarrier cyclic = new CyclicBarrier(N, new BarrierRun(flag, N));
        System.out.println("soldier come!");
        for (int i = 0; i < N; i++) {
            System.out.println("soldier " + i + " come");
            allThreads[i] = new Thread(new Soldier("soldier " + i, cyclic));
            allThreads[i].start();

        }
    }
}
