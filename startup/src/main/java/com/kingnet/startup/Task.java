package com.kingnet.startup;

import android.content.Context;
import android.os.SystemClock;

import com.kingnet.startup.listener.TaskListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

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

    private Executor mExecutorService;//线程池
    private volatile int currentState = STATE_IDLE;//当前状态
    /**
     * 依赖于该Task的Task集合
     */
    private List<Task> mChildTaskList;
    private TaskListener mListener;
    /**
     * 需要等待的次数
     */
    private int mWaitCount;
    private Context mContext;

    public void start(){
        if (currentState != STATE_IDLE) {
            throw new RuntimeException(getTaskName() + "第二次启动！！！");
        }

        long startTime = SystemClock.uptimeMillis();//和System.currentTimeMillis()的区别
        switchState(STATE_WAIT);
        if(null != mListener){
            mListener.onWaitRunning(this);
        }
        Runnable internalRunnable = () -> {
            switchState(STATE_RUNNING);
            long dw = SystemClock.uptimeMillis() - startTime;
            if(null != mListener)
                mListener.onStart(Task.this);
            try {
                Task.this.execute(mContext);
            }catch (Throwable e){
                switchState(STATE_ERROR);
                e.printStackTrace();
                if(null != mListener)
                    mListener.onError(Task.this,e.getMessage());
            }
            switchState(STATE_FINISH);
            long df = SystemClock.uptimeMillis() - startTime;
            if(null != mListener)
                mListener.onFinish(Task.this,dw,df);
            notifyFinished();
        };
        //处理任务
        if(null == mExecutorService)
            throw new IllegalStateException(getTaskName() + "没有设置任务执行者！！！");
        mExecutorService.execute(internalRunnable);
    }

    /**
     * 通知后续任务
     */
    protected void notifyFinished(){
        if(null == mChildTaskList || mChildTaskList.isEmpty())
            return;
        for (Task task : mChildTaskList) {
            task.onDepTaskFinished();
        }

    }

    /**
     * 依赖任务完成，检查
     */
    private void onDepTaskFinished(){
        int size;
        synchronized (this){
            mWaitCount--;
            size = mWaitCount;
        }
        if (size == 0)
            start();
    }

    /**
     * 添加依赖
     * @param task
     */
    public void addDependencies(Task task){
        if(currentState != STATE_IDLE)
            throw new RuntimeException("task" + getTaskName() + " running or error");
        mWaitCount++;
        task.addChildTask(this);
    }

    /**
     * 添加子任务
     * @param task
     */
    private void addChildTask(Task task){
        if (task == this) {
            throw new RuntimeException("A task should not after itself.");
        }
        if (mChildTaskList == null) {
            mChildTaskList = new ArrayList<>();
        }
        mChildTaskList.add(task);
    }



    public List<Task> getChildTaskList() {
        return mChildTaskList;
    }

    public abstract void execute(Context context);
    protected abstract List<Class<? extends Task>> dependencies();


    public String getTaskName(){
        return getClass().getName();
    }

    public boolean isMustInMainThread(){
        return false;
    }


    public void setContext(Context mContext) {
        this.mContext = mContext;
    }


    public void setExecutorService(Executor executor){
        mExecutorService = executor;
    }


    private void switchState(int state){
        currentState = state;
    }

    /**
     * 有依赖于该任务的任务
     * @return
     */
    public boolean hasChild(){
        return null != mChildTaskList && !mChildTaskList.isEmpty();
    }

    public void setListener(TaskListener mListener) {
        this.mListener = mListener;
    }

}
