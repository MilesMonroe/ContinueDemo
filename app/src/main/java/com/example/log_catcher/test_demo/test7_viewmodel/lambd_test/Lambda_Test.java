package com.example.log_catcher.test_demo.test7_viewmodel.lambd_test;

import com.example.log_catcher.util.LogHelper;

/**
 *
 * //用于支持Lambda表达式，需要在gradle->android{}中加入以下:
     compileOptions {
         sourceCompatibility JavaVersion.VERSION_1_8
         targetCompatibility JavaVersion.VERSION_1_8
     }
 *
 *参考网址:https://www.cnblogs.com/weir110/p/9185151.html
 */

//用以声明该接口只能是含有一个函数
@FunctionalInterface
interface WorkerInterface {
    public void doSomeWork();
}

public class Lambda_Test {
    public static void execute(WorkerInterface worker) {
        worker.doSomeWork();
    }

    public static void do_test() {

        //方式1:不使用Lambda表达式
        execute(new WorkerInterface() {
            @Override
            public void doSomeWork() {
                LogHelper.getInstance().w("Worker invoked using Anonymous class");
            }
        });

        //方式2:使用Lambda表达式
        execute(()->{
                        LogHelper.getInstance().w("11123");
                         LogHelper.getInstance().w("456");
                     }
               );
    }

}
