package com.kingnet.startup.factory;

import com.kingnet.startup.Task;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/17 16:00
 * Description:
 */
public interface ITaskFactory {
    Task create(Class<? extends Task> name);
}
