package com.example.log_catcher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataUtil {
    public static boolean putSharedPreferenceString(Context context,String key, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        return editor.commit();
    }
    public static String getSharedPreferenceString(Context context,String key,String defValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return  sp.getString(key,defValue);
    }


    public static boolean putSharedPreferenceInt(Context context,String key, int value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value);
        return editor.commit();
    }
    public static int getSharedPreferenceInt(Context context,String key,int defValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return  sp.getInt(key,defValue);
    }
    public static boolean clearSharedPreference(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }
}
