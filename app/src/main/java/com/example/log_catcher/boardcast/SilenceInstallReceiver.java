package com.example.log_catcher.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.log_catcher.util.LogHelper;

/**
 *   <p>Description: 实现apk更新完成后的自启动效果<br>
 *     &emsp;&emsp;1、需注意manifest.xml中的广播和权限的注册<br></p>
 *   <p>CreatDate: 20200325 <br></p>
 *   <p>author: Miles<br></p>
 *   <p>version: v1.0<br></p>
 *   <p>update: [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]<br></p>
 */
public class SilenceInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
            //packageName为<package:com.example.log_catcher>
            String packageName = intent.getDataString();

            //打印信息如下: 升级了:<package:com.example.log_catcher>包名的程序
            LogHelper.getInstance().w("升级了:" + packageName + "包名的程序");
            if(packageName.equalsIgnoreCase("package:"+context.getPackageName())) {
//                startApp(context);
            }
        }

        // 接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            LogHelper.getInstance().w("安装了:" +packageName + "包名的程序");
            // 监测到升级后执行app的启动 只能启动自身 一般用于软件更新
//            startApp(context);
        }

        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            LogHelper.getInstance().w("卸载了:"  + packageName + "包名的程序");
        }


    }

    public void startApp(Context context){

        LogHelper.getInstance().w("startApp1111");
        //更新完后打开
        Intent it = new Intent();

        //------------显示指定启动的apk--------------
//        程序A中调用程序B的Acitivity不能直接好Intent eulaIntent = new Intent(this, xxx.class);
//        因为xxx是不可见。
//        所以你可以这样用:指定Activity
//        intent.setClassName("com.android.email","com.android.email.xxxActivity");

        //getPackageName= "com.example.log_catcher"
        it.setClassName(context.getPackageName(), context.getPackageName() + ".LogCatcherActivity");    //想要调起的启动活动类


        //------------隐式指定启动的apk--------------
//        it.setAction("android.intent.action.MAIN");                             //首个启动类action
//        it.addCategory("android.intent.category.LAUNCHER");                     //放入程序列表
//        //it.addCategory("android.intent.category.HOME");                           //作为桌面，Home键打开，可做启动默认程序
//        it.addCategory("android.intent.category.START_APP");                      //隐式打开，如果没main可有，如果main可有可无

        //加了这个是重启起一个进程,不加则是以fragment的形式在原进程中添加
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LogHelper.getInstance().w("startApp2222");
        context.startActivity(it);
        LogHelper.getInstance().w("startApp3333");

    }
}
