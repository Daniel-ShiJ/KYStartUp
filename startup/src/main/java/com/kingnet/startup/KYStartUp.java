package com.kingnet.startup;

import android.text.TextUtils;

import com.kingnet.startup.listener.IStartUp;
import com.kingnet.startup.listener.TaskListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 16:59
 * Description:
 */
public class KYStartUp implements IStartUp {
    private CountDownLatch mCountDownLatch;
//    private AtomicInteger mMainTaskCount;

    private MainExecutor mMainExecutor;//主线程
    private ThreadPoolExecutor mThreadPoolExecutor;//子线程
    private List<Task> mTaskList;
    private List<TaskListener> mTaskListener;
    private Map<Class<? extends Task>,Task> mTaskMap;
    Config mConfig;
    LogInfo mLogInfo;

    public KYStartUp(Builder builder) {
        mConfig = builder.mConfig;
        mTaskListener = builder.mTaskListener;
        mTaskList = builder.mTaskList;
        mTaskMap = builder.mTaskMap;

        mConfig = builder.mConfig;
        mLogInfo = builder.mLogInfo;

        mThreadPoolExecutor = builder.mThreadPoolExecutor;

        TaskListener mDefaultListener = new TaskStateListener();


        int mMainTaskCount = 0;
        int mWaitTaskCount = 0;

        for (Task task : mTaskList) {
            task.setStartUp(this);
            if(task.isMustInMainThread()){
                task.setExecutorService(mMainExecutor);
                mMainTaskCount++;
            }else{
                task.setExecutorService(mThreadPoolExecutor);
            }

            if(task.isWaitOnMainThread())
                mWaitTaskCount++;
            task.setListener(mDefaultListener);

        }

    }

    @Override
    public Config getConfig() {
        return mConfig;
    }

    @Override
    public LogInfo getLogInfo() {
        return mLogInfo;
    }


    public static class Builder{
        private Config mConfig;
        private LogInfo mLogInfo;
        private List<Task> mTaskList;
        private Map<Class<? extends Task>,Task> mTaskMap;
        private ThreadPoolExecutor mThreadPoolExecutor;
        private List<TaskListener> mTaskListener;


        public KYStartUp create(){
            if(null == mConfig)
                mConfig = new Config();
            if(null == mLogInfo)
                mLogInfo = new LogInfo.DefaultLogger();
            return new KYStartUp(this);
        }

        public Builder setTaskList(List<Task> mTaskList) {
            this.mTaskList = mTaskList;
            return this;
        }

        public Builder setTaskListener(List<TaskListener> mTaskListener) {
            this.mTaskListener = mTaskListener;
            return this;
        }

        public Builder setTaskMap(Map<Class<? extends Task>, Task> mTaskMap) {
            this.mTaskMap = mTaskMap;
            return this;
        }

        public Builder setThreadPoolExecutor(ThreadPoolExecutor mThreadPoolExecutor) {
            this.mThreadPoolExecutor = mThreadPoolExecutor;
            if(null == mThreadPoolExecutor)
                mThreadPoolExecutor = (ThreadPoolExecutor) Config.getExecutor();
            return this;
        }

        public Builder addTask(Task task){
            if(null == task || TextUtils.isEmpty(task.getTaskName()))
                throw new IllegalStateException("task 不能为空!!!");
            if(null != mTaskMap.get(task.getClass())){
                throw new RuntimeException("重复的task!!");
            }

            if(null == mTaskList)
                mTaskList = new ArrayList<>();
            mTaskList.add(task);
            mTaskMap.put(task.getClass(),task);

            return this;
        }


    }


    private static class TaskStateListener implements TaskListener{

        @Override
        public void onWaitRunning(Task task) {

        }

        @Override
        public void onStart(Task task) {

        }

        @Override
        public void onFinish(Task task, long dw, long df) {

        }
    }
}
