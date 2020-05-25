package com.example.log_catcher.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.log_catcher.util.LogHelper;

/**
 * <p>Description: 实现apk开机自启的广播<br>
 *   &emsp;&emsp;1、需注意manifest.xml中的广播和权限的注册<br></p>
 * <p>CreatDate: 20200325 <br></p>
 * <p>author: Miles<br></p>
 * <p>version: v1.0<br></p>
 * <p>update: [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]<br></p>
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        String packageName = intent.getDataString();
        //接收开机完成广播
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            LogHelper.getInstance().w("123监测到开机启动:《"  + packageName + "》包名的程序");
            startApp(context);
        }
        LogHelper.getInstance().w("456监测到开机启动:《"  + packageName + "》包名的程序");
        Toast.makeText(context,"boot completed",Toast.LENGTH_SHORT).show();


    }

    public void startApp(Context context){

        LogHelper.getInstance().w("startApp1111");
        //更新完后打开
        Intent it = new Intent();

//        还有在程序A中调用程序B的Acitivity不能直接好Intent eulaIntent = new Intent(this, xxx.class);
//        因为xxx是不可见。

//        要这样指定Activity你可以用
//        intent.setClassName("com.android.email",
//                "com.android.email.xxxActivity");

        //getPackageName= "com.example.log_catcher"
        it.setClassName(context.getPackageName(), context.getPackageName() + ".LogCatcherActivity");    //启动类

        it.setAction("android.intent.action.MAIN");                             //首个启动类action
        it.addCategory("android.intent.category.LAUNCHER");                     //放入程序列表
//        it.addCategory("android.intent.category.HOME");                           //作为桌面，Home键打开，可做启动默认程序
        it.addCategory("android.intent.category.START_APP");                      //隐式打开，如果没main可有，如果main可有可无

        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LogHelper.getInstance().w("startApp2222");
        context.startActivity(it);
        LogHelper.getInstance().w("startApp3333");

    }
}
