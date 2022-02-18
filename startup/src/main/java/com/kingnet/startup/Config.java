package com.kingnet.startup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 15:02
 * Description:配置
 *
 */
public class Config {

    public static boolean isStrictMode;

    private static ExecutorService mExecutor;
    private static int mCoreThreadNum = Runtime.getRuntime().availableProcessors();//获取CPU核心数
    private static int mMaxPoolSize = mCoreThreadNum * 2;//队列最大值
    private static long mKeepAliveTime = 60L;//保活时间
    private static ThreadFactory mThreadFactory;//线程工厂

    public static ExecutorService getExecutor() {
        if(null == mExecutor)
            mExecutor = getDefaultExecutor();
        return mExecutor;
    }

    private static ExecutorService getDefaultExecutor() {
        ThreadPoolExecutor defaultExecutor = new ThreadPoolExecutor(mCoreThreadNum,mMaxPoolSize,mKeepAliveTime,TimeUnit.SECONDS,
                new LinkedBlockingQueue(),getThreadFactory());
        return defaultExecutor;
    }

    private static ThreadFactory getThreadFactory() {
        if(null == mThreadFactory)
            mThreadFactory = getDefaultThreadFactory();
        return mThreadFactory;
    }

    private static ThreadFactory getDefaultThreadFactory() {
        ThreadFactory factory = new ThreadFactory() {
            private final AtomicInteger mInteger = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"KY-StartUp Thread-"+mInteger.getAndIncrement());
            }
        };
        return factory;
    }
}
