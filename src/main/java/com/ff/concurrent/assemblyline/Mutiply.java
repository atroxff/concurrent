package com.ff.concurrent.assemblyline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-16 16:57:34
 */
public class Mutiply implements Runnable{

        public static BlockingQueue<Msg> bq = new LinkedBlockingDeque<>();

        @Override
        public void run() {
            while (true) {
                try {
                    Msg msg = bq.take();
                    msg.i = msg.i * msg.j;
                    Div.bq.add(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
