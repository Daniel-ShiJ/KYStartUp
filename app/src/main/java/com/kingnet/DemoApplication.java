package com.kingnet;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;

import com.kingnet.startup.AndroidStartUp.AAndroidStartUp;
import com.kingnet.startup.AndroidStartUp.BAndroidStartUp;
import com.kingnet.startup.AndroidStartUp.CAndroidStartUp;
import com.kingnet.startup.AndroidStartUp.DAndroidStartUp;
import com.kingnet.startup.AndroidStartUp.EAndroidStartUp;
import com.kingnet.startup.AndroidStartUp.FAndroidStartUp;
import com.kingnet.startup.AndroidStartUp.GAndroidStartUp;
import com.kingnet.startup.AndroidStartUp.HAndroidStartUp;
import com.kingnet.startup.Initializer.AInitializer;
import com.kingnet.startup.KYStartUp;
import com.kingnet.startup.listener.IStartUp;
import com.kingnet.startup.listener.StartUpListener;
import com.kingnet.startup.tasks.ATask;
import com.kingnet.startup.tasks.BTask;
import com.kingnet.startup.tasks.CTask;
import com.kingnet.startup.tasks.CustomTaskFactory;
import com.kingnet.startup.tasks.DTask;
import com.kingnet.startup.tasks.ETask;
import com.kingnet.startup.tasks.FTask;
import com.kingnet.startup.tasks.GTask;
import com.kingnet.startup.tasks.HTask;
import com.rousetime.android_startup.StartupManager;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/15 09:28
 * Description:
 */
public class DemoApplication extends Application {

    public static long startTime;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        startTime = SystemClock.uptimeMillis();


//        普通
//        new ATask().execute(base);
//        new BTask().execute(base);
//        new CTask().execute(base);
//        new DTask().execute(base);
//        new ETask().execute(base);
//        new FTask().execute(base);
//        new GTask().execute(base);
//        new HTask().execute(base);


        //StartUp
        KYStartUp.Builder builder = new KYStartUp.Builder()
                .setTaskFactory(new CustomTaskFactory())
                .createTask(ATask.class)
                .addTask(new BTask())
                .addTask(new CTask())
                .addTask(new DTask())
                .addTask(new ETask())
                .addTask(new FTask())
                .addTask(new GTask())
                .addTask(new HTask())
                .setStartUpListener(new StartUpListener() {
                    @Override
                    public void onStartUpStartListener(long time) {
                    }

                    @Override
                    public void onStartUpErrorListener(String message) {
                    }

                    @Override
                    public void onStartUpFinishListener(long time) {
                    }
                });
        IStartUp startUp = builder.create(this);
        startUp.start();


//         AndroidStartUp 第一种配置方式
//        new StartupManager.Builder()
//                .addStartup(new AAndroidStartUp())
//                .addStartup(new BAndroidStartUp())
//                .addStartup(new CAndroidStartUp())
//                .addStartup(new DAndroidStartUp())
//                .addStartup(new EAndroidStartUp())
//                .addStartup(new FAndroidStartUp())
//                .addStartup(new GAndroidStartUp())
//                .addStartup(new HAndroidStartUp())
//                .build(this)
//                .start()
//                .await();
    }
}
