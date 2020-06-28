package com.example.log_catcher.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.WindowManager;


/**该类用于系统相关的设置，本类试验于Android 7.0版本
 *
 * 1、系统唤醒接口wakeAndUnlock:
 *    需要在AndroidManifest.xml中加入以下两个权限:
 *     <uses-permission android:name="android.permission.WAKE_LOCK" />
 *     <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
 * 2、
 */
public class SystemSettingUtils {

    //锁屏、唤醒相关
    private KeyguardManager km;
    private KeyguardManager.KeyguardLock kl;
    private PowerManager pm;
    private PowerManager.WakeLock my_wl;
    private Context mContext;

    public SystemSettingUtils(Context context) {
        this.mContext= context;
    }

    /**休眠/唤醒的设置
     *
     * @param b  true为唤醒亮屏，fasle为休眠息屏
     */
    public void wakeAndUnlock(boolean b)
    {
        if(b)
        {
            //获取电源管理器对象
            pm=(PowerManager) mContext.getSystemService(Context.POWER_SERVICE);

            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            my_wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "maymap:ET16 bright");

            //点亮屏幕
            my_wl.acquire();

            //得到键盘锁管理器对象
            km= (KeyguardManager)mContext.getSystemService(Context.KEYGUARD_SERVICE);
            kl = km.newKeyguardLock("unLock");

            //解锁
            kl.disableKeyguard();
        }
        else
        {
            //锁屏
            kl.reenableKeyguard();

            //释放wakeLock，关灯
            my_wl.release();
        }
    }

    /**控制虚拟按键导航栏的显示
     *
     * @param setting  true为显示，fasle 为隐藏
     */
    void showNavigationBar(Boolean setting){

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        int flag = 3;
		intent.putExtra("flag", flag);
		intent.putExtra("mainkeys", setting);
        mContext.sendBroadcast(intent);
        intent = new Intent();
		intent.setAction("HideNavigation_action");

		intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
		intent.putExtra("cmd", setting?"show":"hide");
        mContext.sendOrderedBroadcast(intent,null);
    }

    /**下拉提示框使能
     *
     * @param setting  true为显示，fasle 为隐藏
     */
    void showDropBar(Boolean setting){

        Intent intent = new Intent();
        intent = new Intent();
        intent.setAction("com.config");
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        //disable status bar
        int flag = 6;
        intent.putExtra("flag", flag);
        intent.putExtra("statusbar", setting);    //true为允许，false为禁止
        mContext.sendBroadcast(intent);
    }

    /**
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }
}
