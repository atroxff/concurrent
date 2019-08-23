package com.ff.concurrent.futuredemo;

import java.util.concurrent.Callable;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-16 15:57:45
 */
public class RealData implements Callable<String> {

    private String para;

    public RealData(String para) {
        this.para = para;
    }

    @Override
    public String call() throws Exception {
        System.out.println(System.currentTimeMillis()+":start");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append(para);
        }
        System.out.println(System.currentTimeMillis()+":end");
        Thread.sleep(10000);
        System.out.println(System.currentTimeMillis()+":future wait finish:");
        return sb.toString();
    }
}
