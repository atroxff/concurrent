package com.ff.concurrent.sort;

import java.util.Arrays;

/**
 * @description:串行奇偶排序
 * @auther: hefeng
 * @creatTime: 2019-8-19 09:49:28
 */
public class OddEvenSortOrign {

    public static void oddEvenSort(int[] arr) {
        int exchFlag = 1;
        int start = 0;
        while (exchFlag == 1 || start == 1) {
            exchFlag = 0;
            for (int i = start; i < arr.length - 1; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    int t = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = t;
                    exchFlag = 1;
                }
            }
            if (start == 1) {
                start = 0;
            }else {
                start = 1;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 9, 5, 4, 6, 7, 2, 1, 6, 3};
        oddEvenSort(a);
        System.out.println(Arrays.toString(a));

    }
}
