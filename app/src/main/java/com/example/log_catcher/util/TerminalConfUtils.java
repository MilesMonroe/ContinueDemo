package com.example.log_catcher.util;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import com.example.log_catcher.bean.AppInfoBean;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;

public class TerminalConfUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 获取手机内部存储空间
     *
     * @return 以M, G为单位的容量
     */
    public static String getInternalMemorySize() {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        return Formatter.formatFileSize(mContext, size);
    }

    /**
     * 获取手机内部可用存储空间
     *
     * @return 以M, G为单位的容量
     */
    public static String getAvailableInternalMemorySize() {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return Formatter.formatFileSize(mContext, availableBlocksLong
                * blockSizeLong);
    }

    /**
     * 获取手机内部已用存储空间
     *
     * @return 以M, G为单位的容量
     */
    public static String getUsedInternalMemorySize() {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        return Formatter.formatFileSize(mContext, size - availableBlocksLong
                * blockSizeLong);
    }

    /**
     *   * 获取android当前可用运行内存大小
     *   * @param context
     *   *
     */
    public static String getAvailMemory() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(mContext, mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获取android当前可用运行内存大小
     *  
     */
    public static String getTotalMemory() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(mContext, mi.totalMem);// 将获取的内存大小规格化
    }

    /**
     * 获取android当前已用运行内存大小
     *  
     */
    public static String getUsedMemory() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(mContext, mi.totalMem - mi.availMem);// 将获取的内存大小规格化
    }

//    //电池电量获取
//    public static int getBattery() {
//        BatteryManager manager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
//        return manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);///当前电量百分比
//    }
    /**
     * 获取当前电量
     * <p>
     * adb shell "cat /sys/class/power_supply/battery/capacity"
     */
    public static int getBatteryCapacity() {
        int result = 0;
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/sys/class/power_supply/battery/capacity"));
            if ((line = br.readLine()) != null) {
                result = Integer.parseInt(line);
                //Log.w(TAG2,"getCurrentChargingCurrent="+result);
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    //获取wifi名字
    public static String getWiFiName() {
        WifiManager wifiMgr = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String ssid = info != null ? info.getSSID() : null;
        if (ssid != null) {
            return ssid.replace("\"", "");
        } else {
            return ssid;
        }
    }

    //获取wifi强度
    public static String getWiFiRssi() {
        WifiManager wifiMgr = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        int ssid = info != null ? info.getRssi() : null;
        return ssid + "";
    }

    //获取sim卡号
    public static String getSimNum() {
        String nativePhoneNumber = "N/A";
        TelephonyManager systemService = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return nativePhoneNumber;
        }
        nativePhoneNumber = systemService.getLine1Number();
        return nativePhoneNumber;
    }

    //获取运营商信息
    public static String getProvidersName() {
        String providersName = "N/A";
        TelephonyManager systemService = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return providersName;
        }
        //IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        /*if (networkOperator.equals("46000") || networkOperator.equals("46002")) {
            providersName = "中国移动";//中国移动
        } else if (networkOperator.equals("46001")) {
            providersName = "中国联通";//中国联通
        } else if (networkOperator.equals("46003")) {
            providersName = "中国电信";//中国电信
        }*/
        return getNetworkOperator(systemService.getNetworkOperator());
    }

    /**
     * 设备是否在充电
     * 0-未在充电中
     * 1-充电中
     * adb shell "cat /sys/class/power_supply/battery/status"
     */
    public static int getBatteryStatus() {
        int result = 0;
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/sys/class/power_supply/battery/status"));
            if ((line = br.readLine()) != null) {
                if ("Charging".equals(line)) {
                    result = 1;
                } else {
                    result = 0;
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static List<AppInfoBean> getAppList() {
        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        ArrayList<AppInfoBean> appInfoBeans = new ArrayList<>();

        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
            {
                AppInfoBean appInfoBean = new AppInfoBean();
                appInfoBean.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
                appInfoBean.setAppVersion(packageInfo.versionName);
                appInfoBeans.add(appInfoBean);
            }
        }
        return appInfoBeans;
    }


    public static String getApkPackageName(String filePath) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String packname = appInfo.packageName;
            String version = info.versionName;
            return packname;
        } else {
            return "";
        }
    }

    public static String getNetworkOperator(String IMSI) {
        String providersName = IMSI;
        switch (IMSI) {
            case "40417":
            case "40425":
            case "40428":
            case "40429":
            case "40437":
            case "40491":
            case "405082":
            case "405800":
            case "405801":
            case "405802":
            case "405803":
            case "405804":
            case "405805":
            case "405806":
            case "405807":
            case "405808":
            case "405809":
            case "405810":
            case "405811":
            case "405812":
            case "405813":
                providersName = "India,AIRCEL";
                break;
            case "40460":
                providersName = "India,Aircell Digilink";
                break;
            case "40415":
                providersName = "India,Aircell Digilink Essar Cellph.";
                break;
            case "40406":
            case "40410":
            case "40431":
            case "40440":
            case "40445":
            case "40449":
            case "40470":
            case "40494":
            case "40495":
            case "40497":
            case "40498":
            case "40551":
            case "40552":
            case "40553":
            case "40554":
            case "40555":
            case "40556":
            case "40570":
                providersName = "India,AirTel";
                break;
            case "40496":
                providersName = "India,Airtel - Haryana";
                break;
            case "40402":
                providersName = "India,Airtel - Punjab";
                break;
            case "40403":
                providersName = "India,Airtel / Bharti Telenet";
                break;
            case "40493":
                providersName = "India,Airtel Gujrat";
                break;
            case "40490":
                providersName = "India,Airtel Maharashtra & Goa";
                break;
            case "40492":
                providersName = "India,Airtel Mumbai";
                break;
            case "40443":
                providersName = "India,BPL Mobile Cellular";
                break;
            case "40421":
                providersName = "India,BPL Mobile Mumbai";
                break;
            case "40427":
                providersName = "India,BPL USWest Cellular / Cellular Comms";
                break;
            case "40434":
            case "40438":
            case "40451":
            case "40453":
            case "40454":
            case "40455":
            case "40457":
            case "40458":
            case "40459":
            case "40464":
            case "40471":
            case "40473":
            case "40474":
            case "40475":
            case "40476":
            case "40477":
            case "40480":
            case "40481":
                providersName = "India,BSNL";
                break;
            case "40462":
                providersName = "India,BSNL J&K";
                break;
            case "40472":
                providersName = "India,BSNL Kerala";
                break;
            case "40466":
                providersName = "India,BSNL Maharashtra & Goa";
                break;
            case "40478":
                providersName = "India,BTA Cellcom";
                break;
            case "40448":
                providersName = "India,Dishnet Wireless";
                break;
            case "40482":
                providersName = "India,Escorts";
                break;
            case "40487":
            case "40488":
            case "40489":
                providersName = "India,Escorts Telecom";
                break;
            case "40411":
                providersName = "India,Essar / Sterling Cellular";
                break;
            case "405912":
            case "405913":
            case "405914":
            case "405917":
                providersName = "India,Etisalat DB(cheers)";
                break;
            case "40566":
                providersName = "India,Hutch";
                break;
            case "40486":
            case "40413":
            case "40484":
                providersName = "India,Hutchinson Essar South";
                break;
            case "40419":
            case "405799":
            case "405845":
            case "405848":
            case "405850":
            case "40586":
                providersName = "India,IDEA";
                break;
            case "40412":
                providersName = "India,Idea (Escotel) Haryana";
                break;
            case "40456":
                providersName = "India,Idea (Escotel) UP West";
                break;
            case "40404":
                providersName = "India,IDEA CELLULAR - Delhi";
                break;
            case "40424":
                providersName = "India,IDEA Cellular - Gujarat";
                break;
            case "40422":
                providersName = "India,IDEA Cellular - Maharashtra";
                break;
            case "405855":
            case "405864":
            case "405865":
                providersName = "India,Loop Mobile";
                break;
            case "40468":
                providersName = "India,MTNL - Delhi";
                break;
            case "40469":
                providersName = "India,MTNL - Mumbai";
                break;
            case "40450":
            case "40452":
            case "40467":
            case "40483":
            case "40485":
            case "40501":
            case "40503":
            case "40504":
            case "40509":
            case "40510":
            case "40513":
                providersName = "India,Reliance";
                break;
            case "40409":
            case "40436":
                providersName = "India,Reliance Telecom Private";
                break;
            case "40441":
                providersName = "India,RPG MAA";
                break;
            case "405881":
                providersName = "India,S Tel";
                break;
            case "40444":
                providersName = "India,Spice Telecom - Karnataka";
                break;
            case "40414":
                providersName = "India,Spice Telecom - Punjab";
                break;
            case "40442":
                providersName = "India,Srinivas Cellcom / Aircel";
                break;
            case "40407":
                providersName = "India,TATA Cellular / Idea Cellular";
                break;
            case "405025":
            case "405026":
            case "405027":
            case "405029":
            case "405030":
            case "405031":
            case "405032":
            case "405033":
            case "405034":
            case "405035":
            case "405036":
            case "405037":
            case "405038":
            case "405039":
            case "405040":
            case "405041":
            case "405042":
            case "405043":
            case "405044":
            case "405045":
            case "405046":
            case "405047":
                providersName = "India,TATA Teleservice";
                break;
            case "405818":
            case "405819":
            case "405820":
            case "405821":
            case "405822":
            case "405844":
            case "405875":
            case "405880":
            case "405927":
            case "405929":
                providersName = "India,Uninor";
                break;
            case "405824":
            case "405827":
            case "405834":
                providersName = "India,Videocon Datacom";
                break;
            case "40420":
            case "40446":
                providersName = "India,Vodafone";
                break;
            case "40405":
            case "40401":
            case "40430":
                providersName = "India,Vodafone - Kolkata";
                break;
            case "405750":
            case "405751":
            case "405752":
            case "405753":
            case "405754":
            case "405755":
            case "405756":
                providersName = "India,Vodafone IN";
                break;
        }
        return providersName;
    }
}
