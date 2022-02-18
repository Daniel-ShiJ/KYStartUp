package com.kingnet.startup.Initializer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.kingnet.startup.tasks.FTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/17 17:31
 * Description:
 */
public class GInitializer implements Initializer<Object> {
    @NonNull
    @Override
    public Object create(@NonNull Context context) {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("初始化 GGGGG，Thread = " + Thread.currentThread());
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        List list = new ArrayList();
        list.add(FInitializer.class);

        return list;
    }
}
