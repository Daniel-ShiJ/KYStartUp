package com.kingnet.startup;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;

import com.kingnet.DemoApplication;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("init 所用时间 ---->>> " + (SystemClock.uptimeMillis() - DemoApplication.startTime));
    }
}