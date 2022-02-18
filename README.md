对于Andorid的启动优化，最知名的应该属Google 在2020年推出的JetPack中的StartUp，拜读源码后，发现Google大佬们，主要是做了两个方向的优化：

1. 初始化前置
2. 通过一个ContentProvider统一管理

翻阅了一些资料，包括但不限于阿里早年的Alpha、AndroidStartUp等，发现Google的StartUp可以有几个优化的地方，基于“实践出真理”，就着手写一写。如果有考虑不周全或是设计错误的地方，望指出，共同进步。

一、首先解决任务执行顺序的问题
部分任务存在依赖关系（先不考虑线程的问题，第二步再来处理线程的问题）。

1. A和H可以同时执行
2. B和E需要等A执行完后，才能执行...以此类推，最后执行G。

解决方案：拓扑排序


二、再来解决线程问题

Task执行有两种可能

- 在主线程执行
- 在子线程执行

1、需要在主线程执行的Task，放入一个只存放必须要主线程执行的队列

2、子线程Task放入一个线程池

 

测试

    public class ATask extends Task {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("初始化 AAAAA");
        }
    
        @Override
        protected List<Class<? extends Task>> dependencies() {
            return null;
        }
    
        @Override
        public boolean isMustInMainThread() {
            return true;
        }
    }
    
    public class BTask extends Task {
        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("初始化 BBBBB");
        }
        @Override
        protected List<Class<? extends Task>> dependencies() {
            List list = new ArrayList();
            list.add(ATask.class);
            return list;
        }
    
        @Override
        public boolean isMustInMainThread() {
            return false;
        }
    }

测试结果

KYStartUp 平均时间：713ms
AndroidStartUp 平均时间：726ms
Jetpack StartUp 平均时间：1511ms
传统做法 平均时间：1629ms

后续

- 架构还需优化的地方
  - 排序的算法优化
  - 数据结构的选择
- 多进程处理？

参考：

https://github.com/alibaba/alpha

https://github.com/idisfkj/android-startup
