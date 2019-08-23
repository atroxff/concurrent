package com.ff.concurrent.assemblyline;

/**
 * @description:流水线处理大量任务
 * @auther: hefeng
 * @creatTime: 2019-8-16 17:01:16
 */
public class PStreamMain {

    public static void main(String[] args) {
        new Thread((new Plus())).start();
        new Thread((new Mutiply())).start();
        new Thread((new Div())).start();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Msg msg = new Msg();
                msg.i = i;
                msg.j = j;
                msg.orgStr = "(" + i + "+" + j + ")*" + i + "/2";
                Plus.bq.add(msg);
            }
        }
    }
}
