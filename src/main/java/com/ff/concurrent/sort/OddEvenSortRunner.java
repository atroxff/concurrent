package com.ff.concurrent.sort;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:并行奇偶排序,将每一次的交换都分派一个线程去执行
 * @auther: hefeng
 * @creatTime: 2019-8-19 09:57:58
 */
public class OddEvenSortRunner {
    static int exchFlag = 1;
    static boolean finish = false;
    static int[] arr;

    public static synchronized int getExchFlag() {
        return exchFlag;
    }

    public static synchronized void setExchFlag(int exchFlag) {
        OddEvenSortRunner.exchFlag = exchFlag;
    }
    public static void setArr(int[] arr) {
        OddEvenSortRunner.arr = arr;
    }


    public static class OddEvenSortTask implements Runnable {
        int i;
        CountDownLatch latch;

        public OddEvenSortTask(int i, CountDownLatch latch) {
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (arr[i] > arr[i + 1]) {
                int t = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = t;
                OddEvenSortRunner.setExchFlag(1);
                System.out.println(Thread.currentThread().getName() + ": " + Arrays.toString(arr) + ",i=" + i);
            }
            System.out.println(Thread.currentThread().getName() + ": " + Arrays.toString(arr) + ",i=" + i+",未变化");
            latch.countDown();
        }
    }

    public static void pOddEvenSort(int[] a) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        setArr(a);
        int start = 0;//start并不需要多线程处理，所以不需要同步
        int count = 0;
        while (getExchFlag() == 1 || start == 1) {//连续的一次偶排序+奇排序没有发生交换，证明排序完成
            setExchFlag(0);
            count++;
            System.out.println("第" + count + "轮:" + (start == 0 ? "偶排序" : "奇排序"));
            //偶排序 数组长度奇数时latchNum=len/2-1=4（01 23 45 67）9，数组长度偶数时latchNum=5（01 23 45 67 89）10
            //奇排序 数组长度偶数时latchNum=len/2-1=4（12 34 56 78）9，数组长度偶数时latchNum=4（12 34 56 78）10
            int latchNum = arr.length / 2 - (arr.length % 2 == 0 ? start : 0);
            CountDownLatch latch = new CountDownLatch(latchNum);
            for (int i = start; i < arr.length - 1; i += 2) {
                pool.execute(new OddEvenSortTask(i, latch));
            }
            latch.await();
            if (start == 1)
                start = 0;
            else
                start = 1;
        }
        finish = true;
        pool.shutdown();

    }

    public static void main(String[] args) throws InterruptedException {
        int[] a = {1, 9, 5, 4, 6, 7, 2, 1, 6};
        pOddEvenSort(a);
        if (finish) {
            System.out.println("finish: "+Arrays.toString(a));
            System.out.println("finish: "+Arrays.toString(arr));
        }
    }
}


