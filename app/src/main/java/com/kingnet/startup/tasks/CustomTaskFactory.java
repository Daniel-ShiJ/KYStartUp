package com.kingnet.startup.tasks;

import com.kingnet.startup.Task;
import com.kingnet.startup.factory.ITaskFactory;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/17 16:57
 * Description:
 */
public class CustomTaskFactory implements ITaskFactory {

    @Override
    public Task create(Class<? extends Task> name) {
//        System.out.println("CustomTaskFactory == " + name.getName());
        switch (name.getName()){
            case "com.kingnet.startup.tasks.ATask":
                return new ATask();
            case "B":
                return new BTask();
        }
        return null;
    }
}
