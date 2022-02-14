package com.kingnet.startup.listener;

import com.kingnet.startup.Config;
import com.kingnet.startup.LogInfo;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/14 17:36
 * Description:
 */
public interface IStartUp {
    Config getConfig();
    LogInfo getLogInfo();
}
