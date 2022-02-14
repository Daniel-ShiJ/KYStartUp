package com.kingnet.startup.listener;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 15:51
 * Description:启动监听
 */
public interface StartUpListener {
    void onStartUpStartListener();

    void onStartUpErrorListener();

    void onStartUpFinishListener();
}
