package com.kingnet.startup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Daniel.ShiJ
 * Date:2022/2/15 10:38
 * Description:
 */
public class SortUtils {
    public static List<Task> sortList = new ArrayList<>();
    public static void sort(List<Task> list){
        if(null == list || list.isEmpty())
            return;
        for (int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            if(!sortList.contains(task)){
                sortList.add(task);
            }
            if(task.hasChild()){//有依赖关系的任务
                List<Task> children = task.getChildTaskList();
                for (int i1 = 0; i1 < children.size(); i1++) {//遍历所有依赖于的任务
                    Task child = children.get(i1);
                    if(!sortList.contains(child)){
                        sort(children);
                    }
                }
            }
        }
    }
}
