package com.ff.concurrent.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:NIO构建Echo服务器
 * @auther: hefeng
 * @creatTime: 2019-8-19 13:37:48
 */
public class NIODemo {

    private Selector selector;
    private ExecutorService tp = Executors.newCachedThreadPool();
    public static Map<Socket, Long> time_start = new HashMap<>(10240);

    private void startServer() throws Exception{
        //1.通过工厂方法获得selector实例
        selector = SelectorProvider.provider().openSelector();
        //2.获得SocketChannel实例
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //3.设置为非阻塞模式
        ssc.configureBlocking(false);
        //4.将channel绑定到端口
        //InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(),8000);
        InetSocketAddress isa = new InetSocketAddress(8000);
        ssc.bind(isa);
        //5.ServerSocketChannel绑定到selector上，注册感兴趣的时间为Accept，使selector为Channel服务
        //SelectionKey表示一对Selector和Channel的关系。
        SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

        for (; ; ) {//6.等待分发网络消息
            selector.select();
            //7.获取已经准备好的SelectionKey
            Set readyKeys = selector.selectedKeys();
            Iterator i = readyKeys.iterator();
            long e = 0;
            //8.迭代处理SelectionKey
            while (i.hasNext()) {
                SelectionKey sk = (SelectionKey) i.next();
                //9.处理时移除
                i.remove();

                if (sk.isAcceptable()) {//10.根据状态进行接收、读、写
                    doAccept(sk);
                } else if (sk.isValid() && sk.isReadable()) {
                    if(!time_start.containsKey(((SocketChannel)sk.channel()).socket())){
                        time_start.put(((SocketChannel)sk.channel()).socket(),System.currentTimeMillis());
                        doRead(sk);
                    }
                } else if (sk.isValid() && sk.isWritable()) {
                    doWrite(sk);
                    e=System.currentTimeMillis();
                    long b = time_start.remove(((SocketChannel)sk.channel()).socket());
                    System.out.println("spend:" + (e - b) + "ms");
                }
            }
        }
    }

    private void doWrite(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();
        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(sk);
                return;
            }

            if (bb.remaining() == 0) {
                outq.removeLast();
            }
        } catch (IOException e) {
            System.out.println("Failed to write to client");
            e.printStackTrace();
            disconnect(sk);
        }
        if (outq.size() == 0) {
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void doRead(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;

        try {
            len = channel.read(bb);
            if (len < 0) {
                disconnect(sk);
                return;
            }
        } catch (IOException e) {
            System.out.println("Failed to read from client.");
            e.printStackTrace();
            disconnect(sk);
            return;
        }
        bb.flip();
        tp.execute(new HandleMsg(sk, bb));

    }

    private void disconnect(SelectionKey sk) {
        try {
            SelectableChannel channel = sk.channel();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChannel;

        try {
            //1.生成与客户端通信的通道
            clientChannel = server.accept();
            //2.设置非阻塞
            clientChannel.configureBlocking(false);
            //3.绑定新生成的channel到selector上，并对读操作感兴趣
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            //4.将客户端实例附加到SelectionKey上
            EchoClient echoClient = new EchoClient();
            clientKey.attach(echoClient);

            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress() + ".");

        } catch (IOException e) {
            System.out.println("Failed to accept new client.");
            e.printStackTrace();
        }

    }

    public class HandleMsg implements Runnable {
        SelectionKey sk;
        ByteBuffer bb;

        public HandleMsg(SelectionKey sk, ByteBuffer bb) {
            this.sk = sk;
            this.bb = bb;
        }

        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) sk.attachment();
            echoClient.enqueue(bb);
            sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }
}
