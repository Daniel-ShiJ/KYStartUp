package com.kingnet.startup.listener;

import com.kingnet.startup.Task;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 16:08
 * Description:
 */
public interface TaskListener {
    void onWaitRunning(Task task);
    void onStart(Task task);
    void onFinish(Task task,long dw,long df);
}
