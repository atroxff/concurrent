package com.ff.concurrent.demo;

/**
 * @description:
 * @auther: hefeng
 * @creatTime: 2019-8-16 15:19:47
 */
public class SingleDemo {

    private SingleDemo(){

    }
    //1.类加载时直接生成实例
//    private static SingleDemo instance = new SingleDemo();
//    public static SingleDemo getInstance(){
//        return instance;
//    }

    //2.synchornized延迟加载，有锁开销
//    private static SingleDemo instance = null;
//    private static synchronized SingleDemo getInstance(){
//        if (instance == null) {
//            instance = new SingleDemo();
//        }
//        return instance;
//    }

    //内部类延迟加载，二者结合
    private static class SingleDemoHandler{
         private static SingleDemo instance = new SingleDemo();
    }

    private static SingleDemo getInstance(){
        return SingleDemoHandler.instance;
    }


}
