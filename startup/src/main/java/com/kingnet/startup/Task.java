package com.kingnet.startup;

import android.os.SystemClock;

import com.kingnet.startup.listener.IStartUp;
import com.kingnet.startup.listener.TaskListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 15:45
 * Description:具体任务
 */
public abstract class Task {
    private static final String TAG = "KY Task";
    public static final int STATE_IDLE = 0;
    public static final int STATE_RUNNING = STATE_IDLE + 1;
    public static final int STATE_FINISH = STATE_RUNNING + 1;
    public static final int STATE_WAIT = STATE_FINISH + 1;
    public static final int STATE_ERROR = STATE_WAIT + 1;

    private String mTaskName;//任务名称


    private Executor mExecutorService;//线程池
    private volatile int currentState = STATE_IDLE;//当前状态
    private List<Task> mTaskList;
    private TaskListener mListener;

    private int mWaitCount;

    private IStartUp mStartUp;


    void start(){
        if(currentState != STATE_IDLE){
            throw new RuntimeException("You try to run task" + mTaskName + " twice, is there a circular dependency?");
        }

        long startTime = SystemClock.uptimeMillis();//和System.currentTimeMillis()的区别
        switchState(STATE_WAIT);
        if(null != mListener){
            mListener.onWaitRunning(this);
        }
        Runnable internalRunnable = new Runnable() {
            @Override
            public void run() {
                switchState(STATE_RUNNING);
                long dw = SystemClock.uptimeMillis() - startTime;
                if(null != mListener)
                    mListener.onStart(Task.this);
                try {
                    run();
                }catch (Throwable e){
                    if(null != mStartUp) {
                        if (mStartUp.getConfig().isStrictMode) {
                            throw e;
                        } else {
                            mStartUp.getLogInfo().e(TAG,mTaskName + " -> "+e.getMessage(),e);
                        }
                    }
                }
                switchState(STATE_FINISH);
                long df = SystemClock.uptimeMillis() - startTime;
                if(null != mListener)
                    mListener.onFinish(Task.this,dw,df);
                notifyFinished();
            }
        };
        //需要多线程处理
        mExecutorService.execute(internalRunnable);
    }

    protected void notifyFinished(){
        if(null == mTaskList || mTaskList.isEmpty())
            return;
        //dijkstra排序

        for (Task task : mTaskList) {
            task.onDepTaskFinished();
        }

    }

    private void onDepTaskFinished(){
        int size;
        synchronized (this){
            mWaitCount--;
            size = mWaitCount;
        }
        if (size == 0)
            start();
    }

    public void addTask(Task task){
        if(this == task){
            throw new RuntimeException("");
        }
        if(null == mTaskList)
            mTaskList = new ArrayList<>();
        mTaskList.add(task);
    }

    abstract void run();
    abstract boolean isMustInMainThread();
    abstract boolean isWaitOnMainThread();

    public void setExecutorService(Executor executor){
        mExecutorService = executor;
    }


    private void switchState(int state){
        currentState = state;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    /**
     * 设置启动对象
     * @param mStartUp
     */
    public void setStartUp(IStartUp mStartUp) {
        this.mStartUp = mStartUp;
    }

    public void setListener(TaskListener mListener) {
        this.mListener = mListener;
    }
}
