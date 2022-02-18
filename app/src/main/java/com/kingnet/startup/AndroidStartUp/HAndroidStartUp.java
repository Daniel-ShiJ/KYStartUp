package com.kingnet.startup.AndroidStartUp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingnet.startup.Initializer.AInitializer;
import com.kingnet.startup.Initializer.CInitializer;
import com.kingnet.startup.Initializer.DInitializer;
import com.rousetime.android_startup.AndroidStartup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/18 13:48
 * Description:
 */
public class HAndroidStartUp extends AndroidStartup<Object> {
    @Nullable
    @Override
    public Object create(@NonNull Context context) {
        System.out.println("初始化 HHHHH，Thread = " + Thread.currentThread());
        return null;
    }

    @Nullable
    @Override
    public List<String> dependenciesByName() {
        List list = new ArrayList();
        list.add(CAndroidStartUp.class.getCanonicalName());
        list.add(DAndroidStartUp.class.getCanonicalName());

        return list;
    }

    @Override
    public boolean callCreateOnMainThread() {
        return false;
    }

    @Override
    public boolean waitOnMainThread() {
        return false;
    }
}
