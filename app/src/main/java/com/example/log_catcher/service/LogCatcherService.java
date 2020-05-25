package com.example.log_catcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.example.log_catcher.util.LogHelper;


public class LogCatcherService extends Service {

    private Downloadbinder downloadbinder = new Downloadbinder();
    private boolean time_run_flag = false;
    private int time_thread_count = 0;

    public LogCatcherService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        LogHelper.getInstance().w("onBind");
        return  downloadbinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.getInstance().w("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogHelper.getInstance().w("onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        time_run_flag = false;
        LogHelper.getInstance().w("onDestroy");

    }

    public class Downloadbinder extends Binder{

            public void startDownload(){
                LogHelper.getInstance().w("startDownload");
            }

            public int getProgress(){
                LogHelper.getInstance().w("getProgress");
                return 0;
            }
            public int startThread(){
                LogHelper.getInstance().w("startThread");
                //启动时间刷新
                time_run_flag = true;
                time_thread_count++;
                new TimeThread().start();
                return 0;
            }

    }

    public boolean getTime_run_flag() {
        return time_run_flag;
    }

    public void setTime_run_flag(boolean time_run_flag) {
        this.time_run_flag = time_run_flag;
    }

    class TimeThread extends Thread {
        @Override
        public void run() {

//            Bundle bundle = new Bundle();
            do {
                try {
                    Thread.sleep(5*1000);
//                    bundle.putString(ISSUE_TIME_REFRESH, "do nothing");
//                    showHandlerMsg(ISSUE_TIME_REFRESH,bundle);
                    LogHelper.getInstance().w("time_thread：do nothing");
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (time_run_flag);

            LogHelper.getInstance().e("time_thread_count end:thread_count--");
            time_thread_count--;
            LogHelper.getInstance().e("time_thread_count end:thread_count="+time_thread_count);

        }
    }


}
