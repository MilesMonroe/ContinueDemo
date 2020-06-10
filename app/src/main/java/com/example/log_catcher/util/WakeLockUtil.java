package com.example.log_catcher.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public class WakeLockUtil {

    public static PowerManager.WakeLock wakeLock = null;

    @SuppressLint("InvalidWakeLockTag")
    public static void acquireWakeLock(Context context)
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.ON_AFTER_RELEASE | PowerManager.PARTIAL_WAKE_LOCK, "wakeLockUtil");
            // PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的 -- 最常用,保持CPU运转
            // SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
            // SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
            // FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
            // ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
            // ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
            if (null != wakeLock)
            {
                wakeLock.acquire(); // 立即获取电源锁
                // wakeLock.acquire(2000); // 2秒后获取电源锁
            }
        }
    }

    public static void releaseWakeLock()
    {
        if (null != wakeLock)
        {
            wakeLock.release();
            wakeLock = null;
        }
    }


    //==========================以下是网上使用的方法============================
//    //锁屏、唤醒相关
//    private KeyguardManager km;
//    private KeyguardManager.KeyguardLock kl;
//    private PowerManager pm;
//    private PowerManager.WakeLock my_wl;
//    private void wakeAndUnlock(boolean b)
//    {
//
//        if(b)
//        {
//            //获取电源管理器对象
//            pm=(PowerManager) mContex.getSystemService(Context.POWER_SERVICE);
//
//            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
//            my_wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "maymap:ET16 bright");
//
//            //点亮屏幕
//            my_wl.acquire();
//
//            //得到键盘锁管理器对象
//            km= (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
//            kl = km.newKeyguardLock("unLock");
//
//            //解锁
//            kl.disableKeyguard();
//        }
//        else
//        {
//            //锁屏
//            kl.reenableKeyguard();
//
//            //释放wakeLock，关灯
//            my_wl.release();
//        }
//
//    }
}
