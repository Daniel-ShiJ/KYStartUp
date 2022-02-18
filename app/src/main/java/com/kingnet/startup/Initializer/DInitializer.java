package com.kingnet.startup.Initializer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.kingnet.startup.tasks.BTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/17 17:31
 * Description:
 */
public class DInitializer implements Initializer<Object> {
    @NonNull
    @Override
    public Object create(@NonNull Context context) {
        System.out.println("初始化 DDDDD，Thread = " + Thread.currentThread());
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        List list = new ArrayList();
        list.add(BInitializer.class);
        return list;
    }
}
