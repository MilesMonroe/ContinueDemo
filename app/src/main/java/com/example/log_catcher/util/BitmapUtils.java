package com.example.log_catcher.util;

import android.graphics.Bitmap;
import android.graphics.Color;

public class BitmapUtils {
    /**
     * 解析颜色深度为1位的图片二进制数据, 转化为Bitmap对象
     *
     * @param width
     * @param height
     * @param imageData
     *            位图对象
     * @return jpg文件二进制数据, 写入文件即为一张可打开的jpg图片文件
     */
    public static Bitmap parseColorDepthOneBitImageData(byte[] imageData,
                                                        int width, int height) {
        int[] colors = new int[width*height];
        int indexOfColors = 0;
        for (int indexOfImageData = 0; indexOfImageData < imageData.length; indexOfImageData++) {
            int b = imageData[indexOfImageData] & 0xFF;
            for (int indexOfBit = 1; indexOfBit <= 8; indexOfBit++) {
                colors[indexOfColors++] = ((b >> (8 - indexOfBit)) & 0x01) == 0x01 ? Color.BLACK
                        : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

}
