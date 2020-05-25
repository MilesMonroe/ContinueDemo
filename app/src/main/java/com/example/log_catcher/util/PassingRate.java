package com.example.log_catcher.util;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;


/**
 *
 */
public class PassingRate {


    public static void writeTxtToFile(String fileName , String strcontent) {
        File file = null;
        try {
            file = new File(fileName, "passingTime.txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }

        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
}
