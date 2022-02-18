package com.kingnet.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import com.kingnet.DemoApplication;
import com.kingnet.startup.Initializer.AInitializer;
import com.kingnet.startup.factory.ITaskFactory;
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

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("init 所用时间 ---->>> " + (SystemClock.uptimeMillis() - DemoApplication.startTime));
    }
}