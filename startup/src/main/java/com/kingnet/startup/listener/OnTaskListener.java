package com.kingnet.startup.listener;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 15:51
 * Description:任务监听
 */
public interface OnTaskListener {
    void onTaskStartListener();

    void onTaskStageListener();

    void onTaskFinishListener();

    String onTaskErrorListener();
}
