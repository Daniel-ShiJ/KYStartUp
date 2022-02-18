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
public class GTask extends Task {
    @Override
    public void execute(Context context) {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("初始化 GGGGG，Thread = " + Thread.currentThread());
    }
    @Override
    public List<Class<? extends Task>> dependencies() {

        List list = new ArrayList();
        list.add(FTask.class);

        return list;
    }

    @Override
    public boolean isMustInMainThread() {
        return true;
    }

}
