package com.kingnet.startup;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.kingnet.startup.factory.ITaskFactory;
import com.kingnet.startup.factory.TaskWrapper;
import com.kingnet.startup.listener.IStartUp;
import com.kingnet.startup.listener.StartUpListener;
import com.kingnet.startup.listener.TaskListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 16:59
 * Description:启动类
 */
public class KYStartUp implements IStartUp {
    private Context mContext;
    private AtomicInteger mMainTaskCount;//主线程执行的任务数
    private AtomicInteger mTaskCount;//总任务数
    private MainExecutor mMainExecutor;//负责主线程
    private ThreadPoolExecutor mThreadPoolExecutor;//子线程
    private StartUpListener mStartUpListener;//启动监听
    private Map<Class<? extends Task>,Task> mTaskMap;
    private Config mConfig;
    private LogInfo mLogInfo;
    private List<Task> mTaskList;
    private Timer timer;
    private TaskWrapper mTaskWrapper;

    public KYStartUp(Builder builder) {
        mContext = builder.mCon;
        mConfig = builder.mConfig;
        mStartUpListener = builder.mStartUpListener;
        mTaskMap = builder.mTaskMap;
        mLogInfo = builder.mLogInfo;
        mTaskWrapper = builder.mTaskWrapper;
        mThreadPoolExecutor = builder.mThreadPoolExecutor;
        mMainExecutor = builder.mMainExecutor;
        TaskListener mDefaultListener = new TaskStateListener();
        int mMainTaskCount = 0;//需要在主线程执行任务计数
        mTaskList = new ArrayList();
        for (int i = 0; i < builder.mTaskList.size(); i++) {
            Task task = builder.mTaskList.get(i);
            task.setContext(mContext);

            if(task.isMustInMainThread()){
                task.setExecutorService(getMainExecutor());
                mMainTaskCount++;
            }else{
                task.setExecutorService(getThreadPoolExecutor());
            }
            task.setListener(mDefaultListener);

            if(null != task.dependencies() && !task.dependencies().isEmpty()){//有依赖
                for (int i1 = 0; i1 < task.dependencies().size(); i1++) {//遍历依赖
                    Class clazz = task.dependencies().get(i1);
                    Task depTask = mTaskMap.get(clazz);//获取实例对象
                    if(null != depTask){
                        task.addDependencies(depTask);//添加依赖
                    }
                }
            }else{
                mTaskList.add(task);
            }
        }

        if (mMainTaskCount > 0) {
            this.mMainTaskCount = new AtomicInteger(mMainTaskCount);
        }

        long startTime = SystemClock.uptimeMillis();
        SortUtils.sort(mTaskList);
//        System.out.println("DAG 排序所用时间："+(SystemClock.uptimeMillis() - startTime));

        mTaskCount = new AtomicInteger(mTaskMap.size());

        System.out.println("排序后，执行顺序");
        for (int i = 0; i < SortUtils.sortList.size(); i++) {
            System.out.println("[第"+i+"位] == " +SortUtils.sortList.get(i).getTaskName());
        }
        System.out.println();
    }

    public MainExecutor getMainExecutor() {
        if(null == mMainExecutor)
            mMainExecutor = new MainExecutor();
        return mMainExecutor;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        if(null == mThreadPoolExecutor)
            mThreadPoolExecutor = (ThreadPoolExecutor) Config.getExecutor();
        return mThreadPoolExecutor;
    }

    @Override
    public Config getConfig() {
        return mConfig;
    }

    @Override
    public LogInfo getLogInfo() {
        return mLogInfo;
    }

    @Override
    public void start() {
        if(mTaskList.isEmpty())
            return;
        timer = new Timer(SystemClock.uptimeMillis());
        mStartUpListener.onStartUpStartListener(timer.getStartTime());
        for (Task task : mTaskList) {
            task.start();
        }
        startLoopMainExecutor();
    }

    /**
     * 轮询主线程任务
     */
    private void startLoopMainExecutor(){
        while (null != this.mMainTaskCount && this.mMainTaskCount.get() > 0) {
            Runnable runnable = getMainExecutor().take();//这里执行需要在主线程执行的任务
            if (null != runnable)
                runnable.run();//主线程执行
        }
    }

    public static class Builder{
        private Context mCon;
        private Config mConfig;
        private LogInfo mLogInfo;
        private List<Task> mTaskList;
        private Map<Class<? extends Task>,Task> mTaskMap = new HashMap<>();
        private MainExecutor mMainExecutor;
        private ThreadPoolExecutor mThreadPoolExecutor;
        private StartUpListener mStartUpListener;
        private TaskWrapper mTaskWrapper;

        public KYStartUp create(Context con){
            if(null == con)
                throw new RuntimeException("Context 不能为null !!!");
            mCon = con;
            if(null == mConfig)
                mConfig = new Config();
            if(null == mLogInfo)
                mLogInfo = new LogInfo.DefaultLogger();
            return new KYStartUp(this);
        }

        public Builder setStartUpListener(StartUpListener mStartUpListener) {
            this.mStartUpListener = mStartUpListener;
            return this;
        }

        public Builder setTaskMap(Map<Class<? extends Task>,Task> mTaskMap) {
            this.mTaskMap = mTaskMap;
            return this;
        }

        public Builder setThreadPoolExecutor(ThreadPoolExecutor mThreadPoolExecutor) {
            this.mThreadPoolExecutor = mThreadPoolExecutor;
            return this;
        }

        public Builder setMainExecutor(MainExecutor mMainExecutor) {
            this.mMainExecutor = mMainExecutor;
            return this;
        }

        public Builder setLogInfo(LogInfo mLogInfo) {
            this.mLogInfo = mLogInfo;
            return this;
        }

        public Builder setConfig(Config mConfig) {
            this.mConfig = mConfig;
            return this;
        }

        public Builder setTaskFactory(ITaskFactory mTaskFactory) {
            if(null != mTaskFactory)
                mTaskWrapper = new TaskWrapper(mTaskFactory);
            return this;
        }

        public Builder setTaskList(List<Task> mTaskList) {
            if (null == mTaskList || mTaskList.isEmpty())
                throw new RuntimeException("TaskList " + "不能为空！！！");

            this.mTaskList.clear();
            this.mTaskMap.clear();
            for (Task task : mTaskList) {
                addTask(task);
            }
            return this;
        }

        public Builder addTask(Task task){
            if(null == task || TextUtils.isEmpty(task.getTaskName()))
                throw new IllegalStateException("task 不能为空!!!");
            if(null != mTaskMap.get(task.getClass())){
                throw new RuntimeException("重复的task!!");
            }

            if (null == mTaskList)
                mTaskList = new ArrayList<>();
            mTaskList.add(task);
            mTaskMap.put(task.getClass(), task);
            return this;
        }

        public Builder createTask(Class<? extends Task> clazz){
            if(null != mTaskWrapper){
                Task task = mTaskWrapper.createTask(clazz);
                addTask(task);
            }
            return this;
        }
    }

    /**
     * 任务状态监听
     */
    private class TaskStateListener implements TaskListener{

        @Override
        public void onWaitRunning(Task task) {
//            System.out.println(task.getTaskName()+",state = onWaitRunning");
        }

        @Override
        public void onStart(Task task) {
//            System.out.println(task.getTaskName()+",state = onStart");
        }

        @Override
        public void onFinish(Task task, long dw, long df) {
//            System.out.println(task.getTaskName() + ",state = onFinish，用时间：dw = " + dw + "，df = " + df);
            if(task.isMustInMainThread()){
                mMainTaskCount.decrementAndGet();
            }

            if(mTaskCount.decrementAndGet() == 0){//所有任务执行完成
                timer.endTime = SystemClock.uptimeMillis();
                mStartUpListener.onStartUpFinishListener(timer.getUsedTime());
            }
        }

        @Override
        public void onError(Task task, String message) {
//            System.out.println(task.getTaskName() + ",state = onError，reason = " + message);
        }
    }
}
