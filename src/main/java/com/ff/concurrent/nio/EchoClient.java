package com.ff.concurrent.nio;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * @description:Echo客户端，封装outq队列保存需要回复给这个各户端的所有信息
 * @auther: hefeng
 * @creatTime: 2019-8-19 14:01:58
 */
public class EchoClient {

    private LinkedList<ByteBuffer> outq;

    EchoClient() {
        outq = new LinkedList<ByteBuffer>();
    }

    public LinkedList<ByteBuffer> getOutputQueue(){
        return outq;
    }

    public void enqueue(ByteBuffer byteBuffer) {
        outq.addFirst(byteBuffer);
    }
}
