package com.kingnet.startup.AndroidStartUp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingnet.startup.Initializer.AInitializer;
import com.rousetime.android_startup.AndroidStartup;
import com.rousetime.android_startup.Startup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/18 13:48
 * Description:
 */
public class AAndroidStartUp extends AndroidStartup<Object> {
    @Nullable
    @Override
    public Object create(@NonNull Context context) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("初始化 AAAAA，Thread = " + Thread.currentThread());
        return null;
    }

    @Override
    public boolean callCreateOnMainThread() {
        return true;
    }

    @Override
    public boolean waitOnMainThread() {
        return false;
    }
}
