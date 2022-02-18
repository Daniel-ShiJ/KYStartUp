package com.kingnet.startup.tasks;

import android.content.Context;

import com.kingnet.startup.Task;

import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/15 09:46
 * Description:
 */
public class ATask extends Task {
    @Override
    public void execute(Context context) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("初始化 AAAAA，Thread = " + Thread.currentThread());
    }

    @Override
    protected List<Class<? extends Task>> dependencies() {
        return null;
    }

    @Override
    public boolean isMustInMainThread() {
        return true;
    }
}
