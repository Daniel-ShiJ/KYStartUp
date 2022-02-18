package com.kingnet.startup.AndroidStartUp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingnet.startup.Initializer.AInitializer;
import com.rousetime.android_startup.AndroidStartup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/18 13:48
 * Description:
 */
public class EAndroidStartUp extends AndroidStartup<Object> {
    @Nullable
    @Override
    public Object create(@NonNull Context context) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("初始化 EEEEE，Thread = " + Thread.currentThread());
        return null;
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
