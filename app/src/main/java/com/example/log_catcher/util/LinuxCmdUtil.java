package com.example.log_catcher.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//
//
//
//
//
//

/**
 * 本类用于在android端执行linux命令。
 *
 * //---------------示例demo-------------------
 *  void demo(){
 *          List<String> list_cmd = new ArrayList<String>();
 *      list_cmd.add("cd data");
 *      list_cmd.add("touch a.txt");
 *      LinuxCmdUtil.executeLinuxCmd(list_cmd);
 *   }
 */
public class LinuxCmdUtil {

    private static final String TAG2 = LinuxCmdUtil.class.getSimpleName();//LinuxCmdUtil

    public static List<String> executeLinuxCmd(List<String> commands) {

        List<String> rspList = new ArrayList<String>();
        Runtime run = Runtime.getRuntime();
        try {
//            Process proc = run.exec("/bin/bash", null, null);
            Process proc = run.exec("/system/bin/sh", null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands) {
                out.println(line);
            }
            // out.println("cd /home/test");
            // out.println("pwd");
            // out.println("rm -fr /home/proxy.log");
            out.println("exit");// 这个命令必须执行，否则in流不结束。
            String rspLine = "";
            while ((rspLine = in.readLine()) != null) {
//                System.out.println(rspLine);
                Log.w(TAG2,rspLine);
                rspList.add(rspLine);
            }
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rspList;
    }


    /** 判断设备是否具有root权限
     * @return   true  - 不具有root权限
     *           false - 不具有root权限
     */
    public static Boolean requestRoot() {

        Process process = null;
        try {
            // Preform su to get root privledges
            process = Runtime.getRuntime().exec("su");
//            process = Runtime.getRuntime().exec("/system/bin/sh");

            // confirm that we have root
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes("echo hello\n");

            // Close the terminal
            outputStream.writeBytes("exit\n");
            outputStream.flush();

            //等程序响应
            process.waitFor();
            if (process.exitValue() != 0) {
                LogHelper.getInstance().w("obtain root fail!");
                return false;
            } else {
                // 记录结果
                // PreferenceHelper.setJellybeanRootRan(context);
                return true;
            }

        }
        catch (IOException e) {

            LogHelper.getInstance().w("IOException: Cannot obtain root");
            e.printStackTrace();
            return false;

        }
        catch (InterruptedException e) {

            LogHelper.getInstance().w("InterruptedException :Cannot obtain root");
            e.printStackTrace();
            return false;
        }

    }
}
