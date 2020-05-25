package com.example.log_catcher.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.example.log_catcher.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;


/**
 * <p>Description: 用于apk的相关使用<br>
 *   &emsp;&emsp;目前支持以下操作：<br>
 *   &emsp;&emsp;1、常规APK安装：<br>
 *   &emsp;&emsp;2、静默APK安装（需要系统应用权限）<br>
 *   &emsp;&emsp;3、Apk之间的调用<br></p>
 * <p>CreatDate: 20200324 <br></p>
 * <p>author: Miles<br></p>
 * <p>version: v1.0<br></p>
 * <p>update: [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]<br></p>
 */
public class ApkUtils {

    /**APK常规安装方法（本方法若是系统应用安装也会有提示出现）<br>
     *
     * @param context    上下文
     * @param apkPath    apk包存在路径
     *
     *  <p>
     * 备注：<br>
     * 1、默认apk包需存放在res/xml/provider_apk_paths.xml中的路径或其子目录下<br>
     *   目前是在Environment.getExternalStorageDirectory()/Download     <br>
     *
     * 2、AndroidManifest.xml中添加provider节点属性<br>
     */
    public static void installAPK(Context context, String apkPath) {

        Uri uri = null;
        File apkFile;
        apkFile =  new File(apkPath);
        //如果不存在则 return出去
        if(!apkFile.exists())
        {
            LogHelper.getInstance().e("file is not exist");
            return ;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判断是否是AndroidN以及更高的版本
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LogHelper.getInstance().w(">Build.VERSION.SDK_INT111");
            Uri contentUri = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName()+".provider", apkFile);
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }
        else{
            LogHelper.getInstance().w("<Build.VERSION.SDK_INT222");
//            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }


    /** APK静默安装方法（优势:本方法若是系统应用,安装则不会出现任何提示）<br>
     *
     * @param apkPath  安装包存放路径
     * @return   true:安装成功
     *           false:安装失败
     *
     *  <p>
     * 备注：<br>
     * 1、本方法需要应用为系统应用，否则会失败<br>
     * 2、配合广播SilenceInstallReceiver自启动<br>
     *
     */
    public static  boolean installSilenceApp(String apkPath) {

        Process process = null;

        BufferedReader successResult = null;
        BufferedReader errorResult = null;

        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();

        if(apkPath==null){
            return false;
        }

        try {
            //判断是否是AndroidN以及更高的版本
            //7.0以后版本使用
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                process = new ProcessBuilder("pm", "install","-i", BuildConfig.APPLICATION_ID, "-r", apkPath).start();
            }
            else{//7.0以前版本使用
                process = new ProcessBuilder("pm", "install", "-r", apkPath).start();
            }

            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            //这以下之后log就没打印了，不知道为啥
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
                LogHelper.getInstance().w(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
                LogHelper.getInstance().w(s);
            }
            LogHelper.getInstance().w("-------------333333");
        }
        catch (Exception e) {
            LogHelper.getInstance().e("pm install fail");
            e.printStackTrace();
        }
        finally {

            try {
                if (successResult != null) {
                    successResult.close();
                }

                if (errorResult != null) {
                    errorResult.close();
                }
            }
            catch (Exception e) {
                LogHelper.getInstance().e("BufferedReader close Fail");
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        LogHelper.getInstance().w("-------------result--------------");
        LogHelper.getInstance().w(",successMsg="+successMsg.toString());
        LogHelper.getInstance().w(",errorMsg="+errorMsg.toString());
        LogHelper.getInstance().w("---------------------------------");
        //        Toast.makeText(context,"  "+successMsg , Toast.LENGTH_LONG).show();

        //如果含有“success”单词则认为安装成功
        return successMsg.toString().equalsIgnoreCase("success");

    }

    /**apk启用另一个apk的3种方式<br>
     *
     * @param context     传入的上下文
     * @param packageName 被调用apk的包名，例如： com.android.email
     * @param className   被调用apk的包名的完整名称，例如：com.android.email.xxxActivity
     * @param mode        调用的模式；
     *            <br>    &emsp;&emsp;0：setClassName方式<br>
     *                    &emsp;&emsp;1：setComponent方式<br>
     *                    &emsp;&emsp;2：packManager方式<br>
     */
    public static void callAnotherApk(Context context, @NonNull String packageName, @NonNull String className, int mode){


        if((mode<0)||(mode>2)){
            LogHelper.getInstance().e("mode err");
            return ;
        }

        //--------------------方法1 setClassName---------------
        if(mode==0){
            Intent intent = new Intent();
//        程序A中调用程序B的Acitivity不能直接Intent eulaIntent = new Intent(this, xxx.class);
//        因为xxx是不可见。
//        所以你可以这样用:指定Activity
//        intent.setClassName("com.android.email","com.android.email.xxxActivity");

//        it.setClassName("com.example.myapplication", "com.example.myapplication.MainActivity");//想要调起的启动活动类
            intent.setClassName(packageName, className);//想要调起的启动活动类

            //加了这个是重启起一个进程,不加则是以fragment的形式在原进程中添加
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        //--------------------方法2 setComponent---------------
        else if(mode==1){
            Intent intent1 = new Intent();
            //"com.sunyard.issue_tool_100"必须与applicationId中的一致，而不是包名
            //ComponentName toActivity = new ComponentName("com.sunyard.issue_tool_100", "com.sunyard.issue_tool_100.IssueActivity");//想要调起的启动活动类
            ComponentName toActivity = new ComponentName(packageName, className);//想要调起的启动活动类

            intent1.setComponent(toActivity);
            //        intent.setAction("android.intent.action.MAIN");
            //        intent.setAction("android.intent.action.VIEW");
            //加了这个是重启起一个进程,不加则是以fragment的形式在原进程中添加
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        else{//(mode==2)
            //--------------------方法3 packManager启动---------------
            PackageManager packManager = context.getPackageManager();
            //Intent intent = packManager.getLaunchIntentForPackage("com.example.myapplication");
            Intent intent = packManager.getLaunchIntentForPackage(packageName);

            //        intent.putExtra(key,value);
            context.startActivity(intent);
        }
    }


}
