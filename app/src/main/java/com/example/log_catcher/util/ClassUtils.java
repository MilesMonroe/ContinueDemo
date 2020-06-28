package com.example.log_catcher.util;

public class ClassUtils {

    /**本函数用以确认指定类型是否存在
     *
     * @param className 想到确认是否存在的类名
     * @return
     */
    static boolean hasDependency(String className)
    {
        boolean hasDependency;
        //若没有找到该类名，则会抛出异常
        try {
//            Class.forName("okhttp3.OkHttpClient");
            Class.forName(className);
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
            //在Caller处可以抛出此异常
//            throw new IllegalStateException("Must be dependency Okhttp");

        }
        return hasDependency;
    }
}
