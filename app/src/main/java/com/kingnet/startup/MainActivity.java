package com.kingnet.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //解决任务之间的依赖关系 DAG有向无环图

        //CarDownCount


        /**
         * 1.Executor
         * 2.Task
         * 3.接口
         */
    }
}