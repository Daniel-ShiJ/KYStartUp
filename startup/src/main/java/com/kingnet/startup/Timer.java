package com.kingnet.startup;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/15 17:21
 * Description:
 */
public class Timer {
    long startTime;
    long endTime;
    long usedTime;

    public Timer(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getUsedTime() {
        return endTime - startTime;
    }
}
