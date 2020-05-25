package com.example.log_catcher.util;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    public static int byte2Int(byte[] byteArray) {
        int integer = 0x00;
        int shiftCount = 0;
        for (int i = byteArray.length - 1; i >= 0 && i >= byteArray.length - 4;) {
            byte b = byteArray[i];
            // byte为负数左移后转为int还是负数
            integer |= ((b & 0xFF) << shiftCount);
            i--;
            shiftCount += 8;
        }
        return integer;
    }

    /**
     * 将int转为hex
     * @param i
     * @param length 几位hex, 比如length为1的话15被转为F, 16被转为0
     * @return
     */
    public static String int2Hex(int i, int length) {
        if (length <= 0) {
            throw new RuntimeException("指定的字符串长度不支持");
        }
        long l;
        switch (length) {
            case 1:
                l = 0xFL;
                break;
            case 2:
                l = 0xFFL;
                break;
            case 3:
                l = 0xFFFL;
                break;
            case 4:
                l = 0xFFFFL;
                break;
            case 5:
                l = 0xFFFFFL;
                break;
            case 6:
                l = 0xFFFFFFL;
                break;
            case 7:
                l = 0xFFFFFFFL;
                break;
            default:
                l = 0xFFFFFFFFL;
                break;
        }
        String result = Long.toHexString(l & i);
        StringBuilder builder = new StringBuilder(result);
        for (int j = 0; j < length - result.length(); j++) {
            builder.insert(0, "0");
        }
        return builder.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 将int转为指定位数的字符串, 不足则高位补0
     */
    public static String int2String(int interger, int length) {
        String intStr = String.valueOf(interger);
        if(intStr.length() > length) {
            return intStr.substring(intStr.length() - length, intStr.length());
        } else if(intStr.length() == length) {
            return intStr;
        } else {
            StringBuilder builder = new StringBuilder(intStr);
            for (int i = 0; i < length - intStr.length() ; i++) {
                builder.insert(0, '0');
            }
            return builder.toString();
        }
    }

    @SuppressLint({ "DefaultLocale" })
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);

        for (int i = 0; i < bArray.length; i++) {
            String sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static int byte2int2(byte[] b) {
        return b[1] & 0xFF | (b[0] & 0xFF) << 8;
    }

    public static byte[] concat(byte[] b1, byte[] b2) {
        if (b1 == null)
            return b2;
        if (b2 == null) {
            return b1;
        }
        byte[] result = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, result, 0, b1.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }

    public static int getXB(byte[] bdata2) {
        int i, j = 0, k = 0;
        for (i = 0; i < bdata2.length; i++) {
            if (bdata2[i] == 0x3D || bdata2[i] == 0x44) {
                j = i;
                k++;
                if (k == 1) {
                    return j;
                }
            }
        }
        return j;
    }

    public static byte[] byte2Bcd(byte[] abt) {
        if (abt == null) {
            return null;
        }
        int bbtLen = abt.length / 2;
        if (bbtLen == 0) {
            return new byte[0];
        }
        byte[] bbt = new byte[abt.length / 2];
        int j, k;
        for (int p = 0; p < abt.length / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    @SuppressLint({ "DefaultLocale" })
    public static String byteArrayToString(byte[] data) {
        if (data == null) {
            return null;
        }
        String str = "";
        String tempStr = "";
        if (data.length == 0) {
            return "";
        }
        for (int i = 0; i < data.length; i++) {
            tempStr = byteToString(data[i]);
            if (tempStr.length() == 1)
                tempStr = "0" + tempStr;
            str = str + tempStr;
        }
        return str.toUpperCase();
    }

    @SuppressLint({ "DefaultLocale" })
    private static String byteToString(byte buf) {
        int n = buf >= 0 ? buf : 256 + buf;
        String str = Integer.toHexString(n);
        return str.toUpperCase();
    }

    @SuppressLint({ "DefaultLocale" })
    public static char[] getCharByMethod(char[] a, int i, int j) {
        if (a == null) {
            return null;
        }
        int N = a.length;
        if (N == 0) {
            return new char[0];
        }
        StringBuffer s = new StringBuffer(" ");
        if ((i >= N) || (j >= N) || (i > j)) {
            return s.toString().toCharArray();
        }
        for (int x = i; x < j; x++) {
            s.append(a[x]);
        }
        return s.toString().trim().toCharArray();
    }

    @SuppressLint({ "DefaultLocale" })
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch; // 锟斤拷位16锟斤拷锟斤拷锟斤拷转锟斤拷锟斤拷锟�10锟斤拷锟斤拷锟斤拷
            char hex_char1 = hexString.charAt(i); ////锟斤拷位16锟斤拷锟斤拷锟斤拷锟叫的碉拷一位(锟斤拷位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16; //// 0 锟斤拷Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; //// A 锟斤拷Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///锟斤拷位16锟斤拷锟斤拷锟斤拷锟叫的第讹拷位(锟斤拷位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); //// 0 锟斤拷Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; //// A 锟斤拷Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//锟斤拷转锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷Byte锟斤拷
        }
        return retData;
    }
    /**
     * 该行数在传入字符串不是偶数的时候会在字符串第0位插入一个"0", 再进行后面的操作
     * @param asc
     * @return
     */
    public static byte[] hexString2Bytes(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte[] abt = new byte[len];
        if (len >= 2) {
            len /= 2;
        }
        byte[] bbt = new byte[len];
        abt = asc.getBytes();

        for (int p = 0; p < asc.length() / 2; p++) {
            int j;
            if ((abt[(2 * p)] >= 48) && (abt[(2 * p)] <= 57)) {
                j = abt[(2 * p)] - 48;
            } else {
                if ((abt[(2 * p)] >= 97) && (abt[(2 * p)] <= 122))
                    j = abt[(2 * p)] - 97 + 10;
                else
                    j = abt[(2 * p)] - 65 + 10;
            }
            int k;
            if ((abt[(2 * p + 1)] >= 48) && (abt[(2 * p + 1)] <= 57)) {
                k = abt[(2 * p + 1)] - 48;
            } else {
                if ((abt[(2 * p + 1)] >= 97) && (abt[(2 * p + 1)] <= 122))
                    k = abt[(2 * p + 1)] - 97 + 10;
                else
                    k = abt[(2 * p + 1)] - 65 + 10;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }



    /**
     * bytes字符串转换为Byte值
     * @param  hexString Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase(Locale.US);
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] =
                    (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 16进制字符数据转换为10进制字节数据
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字符串转换成十六进制字符串
     * @return String 每个Byte之间无空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {
        if (str == null || str.equals("")) {
            return "";
        }
        byte[] bs = str.getBytes();
        return byte2HexStr(bs);
    }

    /**
     * 字符串转换成十六进制字符串
     * @param str 待转换的ASCII字符串
     * @param charset 指定字符集
     * @return String 每个Byte之间无空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str, String charset) {
        if (str == null || str.equals("")) {
            return "";
        }
        byte[] bs;
        try {
            bs = str.getBytes(charset);
            return byte2HexStr(bs);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("字符串转byte[]出错, 指定的字符集是: " + charset);
        }
    }

    /**
     * 16进制字符串转十进制数
     * @param str
     * @return
     */
    public static int hexStr2Int(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        return Integer.parseInt(str, 16);
    }

    public static Map<String, String> parseIcUserInfo2(byte[] data, String charsetName) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            int point = 0;
            while (point < data.length - 4) {
                byte[] tagByte = new byte[1];
                byte[] lenByte = new byte[3];
                String tag, tagData;
                tagByte[0] = data[point];
                tag = new String(tagByte);
                point = point + 1;
                System.arraycopy(data, point, lenByte, 0, 3);
                point = point + 3;
                int len = Integer.valueOf(new String(lenByte));
                byte[] tagDataByte = new byte[len];
                System.arraycopy(data, point, tagDataByte, 0, len);
                point = point + len;
                tagData = new String(tagDataByte, charsetName);
                map.put(tag, tagData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static byte[] int2bytes(int value, int size) {
        byte buf[] = new byte[size];
        for (int i = buf.length - 1; i >= 0; i--) {
            int bitCount = 8 * (buf.length - i - 1);
            if (bitCount < 32) {
                buf[i] = (byte) (value >>> bitCount);
            } else {
                buf[i] = 0;
            }
        }
        return buf;
    }

    /**
     * bytes转换成十六进制字符串
     * @return String 每个Byte值之间无空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        if (b != null && b.length > 0) {
            for (int n = 0; n < b.length; n++) {
                stmp = Integer.toHexString(b[n] & 0xFF);
                sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
                sb.append("");
            }
        }
        return sb.toString().toUpperCase(Locale.US).trim();
    }

    /**
     * bytes转换成十六进制字符串
     * @return String 每个Byte值之间无空格分隔
     */
    public static String byte2HexStr(byte[] b, int offset, int length) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        if (b != null && b.length > 0) {
            for (int n = offset; n < length; n++) {
                stmp = Integer.toHexString(b[n] & 0xFF);
                sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
                sb.append("");
            }
        }
        return sb.toString().toUpperCase(Locale.US).trim();
    }

    /**
     * CHK 值计算
     * byte[]进行异或,byte返回；
     * @return 16进制字符串
     */
    public static byte[] byteXOR(byte[] b) {
        byte[] r = new byte[1];
        if (b != null && b.length > 0) {
            for (int i = 0; i < b.length; i++) {
                if (i == 0)
                    r[0] = b[i];
                else
                    r[0] = (byte) (r[0] ^ b[i]);
            }
        }
        return r;
    }

    /**
     * 将  以分为单位的纯数字字符串  转换为  以元为单位的数字字符串
     * @param numStrInUtilsOfCents 以分为单位的纯数字字符串
     * @return 返回以元为单位的数字字符串，该字符串有一个小数点，显示到0后两位，例如0.00
     */
    public static String formatFromCentsToYuan(String numStrInUtilsOfCents) {
        if (numStrInUtilsOfCents == null || numStrInUtilsOfCents.equals("")) {
            return "";
        }

        /*
         * 判断字符串内容是否为纯数字
         */
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(numStrInUtilsOfCents);
        if (!isNum.matches()) {
            throw new RuntimeException("纯数字字符串：\"" + numStrInUtilsOfCents + "\"，中出现非数字字符");
        }

        /*
         * 给位数不够的字符串开头补0
         */
        StringBuilder builder = new StringBuilder(numStrInUtilsOfCents);
        if (builder.length() < 3) {
            builder.insert(0, "00");
        }
        /*
         * 去除高位多余的0
         */
        builder.insert(builder.length() - 2, ".");
        while (builder.charAt(0) == '0' && builder.indexOf(".") != 1) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * byte数组转int数组
     * @param b in 输入的byte[]
     * @return 返回int[]
     */
    public static int[] byteArrayToIntArray(byte[] b) {
        if (b == null) {
            return null;
        }
        int[] data = new int[b.length];
        if (b.length > 0) {
            for (int i = 0; i < b.length; i++) {
                    data[i] = b[i];
            }
        }
        return data;
    }


    /**
     * int数组转 byte数组(损失精度)
     * @param data  in 输入的 int[]
     * @return byte[]
     */
    public static byte[] IntArrayTobyteArray(int[] data) {
        if (data == null) {
            return null;
        }
        byte[] ret = new byte[data.length];
        if (data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                ret[i] = (byte) data[i];
            }
        }
        return ret;
    }

    /**
     * 字符数组 转 String ，截断 \0
     * @param data 字符数组
     * @return String
     */
    public static String strBytesToString(byte[] data){
        int len = data.length;
        for(int i=0; i<data.length;i++){
            if(data[i] == 0x00){
                len = i;
                break;
            }
        }
        byte[] result = new byte[len];
        System.arraycopy(data, 0, result, 0, result.length);
        try {
            return new String(result,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @category BCD码数组转为10进制字符串
     * @param    BCDbytes   BCD数组
     * @return   返回转换后的BCD字符串
     * */
    public static String bcd2Str(byte[] BCDbytes) {
        StringBuffer temp = new StringBuffer(BCDbytes.length * 2);
        for (int i = 0; i < BCDbytes.length; i++) {
            temp.append((byte) ((BCDbytes[i] & 0xf0) >>> 4));
            temp.append((byte) (BCDbytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    /**
     * @category 10进制字符串转为BCD码数组
     * @param    asc   10进制字符串
     * @return   BCD码数组
     * */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }


}
