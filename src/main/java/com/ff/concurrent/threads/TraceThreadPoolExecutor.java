package com.ff.concurrent.threads;

import java.util.concurrent.*;

/**
 * @description:扩展线程池，打印异常堆栈
 * @auther: hefeng
 * @creatTime: 2019-8-16 10:26:54
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    //3.在执行时调用封装后的Runnable
    @Override
    public void execute(Runnable task) {
        super.execute(wrap(task,clientTrace(),Thread.currentThread().getName()));
    }
    //3.
    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task,clientTrace(),Thread.currentThread().getName()));
    }
    //1.自定义异常
    private Exception clientTrace(){
        return new Exception("Client Stack Trace");
    }
    //2.对任务在进行一层封装，生成一个新的Runnable，抛出异常
    private Runnable wrap(final Runnable task, final Exception clientStack, String clientThreadName) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    clientStack.printStackTrace();
                    throw e;
                }

            }
        };
    }
}
