package com.kingnet.startup.tasks;

import android.content.Context;

import com.kingnet.startup.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/15 09:46
 * Description:
 */
public class ETask extends Task {
    @Override
    public void execute(Context context) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("初始化 EEEEE，Thread = " + Thread.currentThread());
    }
    @Override
    protected List<Class<? extends Task>> dependencies() {
        return null;
    }

    @Override
    public boolean isMustInMainThread() {
        return false;
    }

}
