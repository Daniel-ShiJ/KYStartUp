package com.kingnet.startup.factory;

import com.kingnet.startup.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/17 16:14
 * Description:
 */
public final class TaskWrapper{
    private ITaskFactory mFactory;
    private Map<Class<? extends Task>,Task> mMap = new HashMap<>();
    public TaskWrapper(ITaskFactory mFactory) {
        this.mFactory = mFactory;
    }

    public Task createTask(Class clazz){
        Task task = mMap.get(clazz);
        if(null != task)
            return task;
        task = mFactory.create(clazz);
        if(null == task)
            throw new IllegalArgumentException("创建Task失败!!!!");
        mMap.put(clazz,task);
        return task;
    }
}

