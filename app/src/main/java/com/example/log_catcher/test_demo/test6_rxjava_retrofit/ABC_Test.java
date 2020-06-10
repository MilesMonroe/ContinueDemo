package com.example.log_catcher.test_demo.test6_rxjava_retrofit;

import com.example.log_catcher.util.LogHelper;

/**只是测试抽象类用法，可忽略
 *
 */

//用以声明该接口只能是含有一个函数
@FunctionalInterface
interface WorkerInterface {
    public void doSomeWork();
}

public abstract class ABC_Test {
    //子类必覆写
    public abstract void printf();
    //子类可自行选择是否覆写
    public  void printf2(){

    };
    //
    public static void exe(WorkerInterface workerInterface){
        workerInterface.doSomeWork();
    }
    public void test(){
        exe(()->{
                LogHelper.getInstance().w("11123");
                LogHelper.getInstance().w("456");
            });
    }
}
