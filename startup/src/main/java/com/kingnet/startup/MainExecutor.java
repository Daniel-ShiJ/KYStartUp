package com.kingnet.startup;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 16:12
 * Description:
 */
public class MainExecutor implements Executor {
    private BlockingQueue<Runnable> mBlockingQueue;//BlockingQueue这个是什么？

    public MainExecutor() {
        this.mBlockingQueue = new LinkedBlockingQueue<>();//LinkedBlockingQueue这个是什么？
    }

    @Override
    public void execute(Runnable command) {
        mBlockingQueue.offer(command);
    }

    public Runnable take(){
        try {
            return mBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
