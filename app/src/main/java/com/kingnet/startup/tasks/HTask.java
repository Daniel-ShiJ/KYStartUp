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
public class HTask extends Task {
    @Override
    public void execute(Context context) {
        System.out.println("初始化 HHHHH，Thread = " + Thread.currentThread());
    }

    @Override
    protected List<Class<? extends Task>> dependencies() {

        List list = new ArrayList();
        list.add(CTask.class);
        list.add(DTask.class);

        return list;
    }
}
