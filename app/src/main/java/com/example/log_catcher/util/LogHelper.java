package com.example.log_catcher.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;


//import com.sunyard.BuildConfig;

//import com.blackhao.utillibrary.time.TimeUtil;

/**
 * Author ： BlackHao
 * Time : 2017/8/1 14:05
 * Description : Log 工具类 ,支持跳转对应的代码位置
 */
public class LogHelper {

    //单例
    private volatile static LogHelper logUtil;

    //打印等级(这里用于release版本打印)
    private static int LOG_LEVEL = Log.ERROR;

    //Log 单次打印的最大长度
    private static final int MAX_LENGTH = 3 * 1024;


    private Handler handler;
    StringBuffer debugData = new StringBuffer();


    //打印调试开关
//    private static boolean IS_DEBUG = BuildConfig.DEBUG;
    private static boolean IS_DEBUG = true;
//    public  static boolean IS_DEBUG = BuildConfig.DEBUG; //打开写文件功能的条件1；BuildConfig.DEBUG=true

    //是否需要写文件
    private static  boolean IS_WRITE_FILE = false;
//    public static  boolean IS_WRITE_FILE = false;//打开写文件功能的条件2；


    //文件路径
    private static String LOG_FILE_PATH;

    private int showHandlerMsg_flag = 0; //0:不开启showHandlerMsg模式； 1：开启
    //todo
    public void setHandler(Handler handler) {
        this.handler = handler;
        showHandlerMsg_flag = 1;
    }


    protected void showHandlerMsg(String operation, Bundle bundle) {
        Message msg = new Message();
        msg.obj = operation;
        msg.setData(bundle);
        handler.sendMessage(msg);

    }

    //优化后的单例模式，参见https://baijiahao.baidu.com/s?id=1625588808634528054&wfr=spider&for=pc
    public static LogHelper getInstance() {
        if (logUtil == null) {
            logUtil = new LogHelper();
            synchronized (LogHelper.class) {
                //在第一个logUtil实例化出来后,防止后面的已进来的等待线程直接执行logUtil = new LogHelper();而导致出现多个实例
                if (logUtil == null) {
                    logUtil = new LogHelper();
                }
            }
        }
        return logUtil;
    }
//
//    public class Singleton {
//        private volatile static Singleton singleton;
//        private Singleton (){}
//        public static Singleton getSingleton() {
//            if (singleton == null) {
//                synchronized (Singleton.class) {
//                    if (singleton == null) {
//                        singleton = new Singleton();
//                    }
//                }
//            }
//            return singleton;
//        }
//    }
    /**
     * 打开log2File的功能
     */
    public void openLog2File(Context context) {
        if (context != null) {
            //LOG_FILE_PATH=/data/user/0/com.example.myapplication/cache/QChatLog
            LOG_FILE_PATH = context.getCacheDir().getAbsolutePath() + File.separator + "QChatLog";

            this.IS_WRITE_FILE = true;
//            this.IS_DEBUG = true;
        }
    }

    /**
     * 关闭log2File的功能
     */
    public void closeLog2File() {
        LOG_FILE_PATH = null;
        this.IS_WRITE_FILE = false;
//        this.IS_DEBUG = false;
    }

    /**
     * 获取 TAG 信息：文件名以及行数
     *
     * @return TAG 信息
     */
    private synchronized String getTAG() {
        StringBuilder tag = new StringBuilder();
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return "";
        }
        for (StackTraceElement st : sts) {
            //筛选获取需要打印的TAG
            if (!st.isNativeMethod()
                    && !st.getClassName().equals(Thread.class.getName())
                    && !st.getClassName().equals(this.getClass().getName())) {
                //获取文件名以及打印的行数，不需要加打印时间
                if(IS_WRITE_FILE == false){
                    tag.append("(").append(st.getFileName()).append(":").append(st.getLineNumber()).append(")");
                }
                else{//写文件时需要加入时间
                    tag.append("(").append(st.getFileName()).append(":").append(st.getLineNumber()).append(")").append(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss") ).append("_");
                }

                return tag.toString()+"<"+Thread.currentThread().getName()+">";
            }
        }
        return "";
    }

    /**
     * Log.e 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void e(String text) {
        if (IS_DEBUG || Log.ERROR >= LOG_LEVEL) {
            if (TextUtils.isEmpty(text)) {
                Log.e(getTAG(), "Log Error text is null");
                return;
            }
            for (String str : splitStr(text)) {
                Log.e(getTAG(), str);
                writeLog("ERROR : " + getTAG() + " : " + str);
            }
        }
    }

    /**
     * Log.d 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void d(String text) {
        if (IS_DEBUG || Log.DEBUG >= LOG_LEVEL) {
            for (String str : splitStr(text)) {
                Log.d(getTAG(), str);
                writeLog("DEBUG : " + getTAG() + " : " + str);
            }
        }
    }

    /**
     * Log.w 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void w(String text) {
        int temp_length= 0;
        Bundle bundle=new Bundle();
        if (IS_DEBUG || Log.WARN >= LOG_LEVEL) {
            for (String str : splitStr(text)) {
                Log.w(getTAG(), str);
                writeLog("WARN : " + getTAG() + " : " + str);

                if(showHandlerMsg_flag == 1){
                    debugData.append(str+"\n");

                    //在debug窗口显示发送的数据
                    bundle.putString("ISSUE_Debug", debugData.toString());
                    showHandlerMsg("ISSUE_Debug", bundle);

                    //StringBuilder变量的清缓存3
                    temp_length = debugData.length();
                    debugData.delete(0, temp_length);

                }

            }
        }
    }

    /**
     * Log.i 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void i(String text) {
        if (IS_DEBUG || Log.INFO >= LOG_LEVEL) {
            for (String str : splitStr(text)) {
                Log.i(getTAG(), str);
                writeLog("INFO : " + getTAG() + " : " + str);
            }
        }
    }

    /**
     * Log.e 打印格式化后的JSON数据
     *
     * @param json 需要打印的内容
     */
    public synchronized void json(String json) {
        if (IS_DEBUG || Log.ERROR >= LOG_LEVEL) {
            String tag = getTAG();
            try {
                //转化后的数据
                String logStr = formatJson(json);
                for (String str : splitStr(logStr)) {
                    Log.e(getTAG(), str);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(tag, e.toString());
            }
        }
    }

    /**
     * 数据分割成不超过 MAX_LENGTH的数据
     *
     * @param str 需要分割的数据
     * @return 分割后的数组
     */
    private String[] splitStr(String str) {
        //字符串长度
        int length = str.length();
        //返回的数组
        String[] strs = new String[length / MAX_LENGTH + 1];
        //
        int start = 0;
        for (int i = 0; i < strs.length; i++) {
            //判断是否达到最大长度
            if (start + MAX_LENGTH < length) {
                strs[i] = str.substring(start, start + MAX_LENGTH);
                start += MAX_LENGTH;
            } else {
                strs[i] = str.substring(start, length);
                start = length;
            }
        }
        return strs;
    }


    /**
     * 格式化
     *
     * @param jsonStr json数据
     * @return 格式化后的json数据
     * @author lizhgb
     * @link https://my.oschina.net/jasonli0102/blog/517052
     */
    private String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last;
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 在 StringBuilder指定位置添加 space
     *
     * @param sb     字符集
     * @param indent 添加位置
     * @author lizhgb
     * @link https://my.oschina.net/jasonli0102/blog/517052
     */
    private void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    /**
     * 将 log 写入文件
     *
     * @param log 需要写入文件的 log
     */
    private void writeLog(String log) {
        try {
            if (!IS_WRITE_FILE) {
//                throw new Exception("need  <IS_WRITE_FILE> is TURE");
//                Log.w("LogHelper writeLog", "need  <IS_WRITE_FILE> is TURE");
                return;
            }
            if (LOG_FILE_PATH == null) {
//                throw new Exception("need call <initLogFile>");
                Log.w("LogHelper writeLog", "need call <initLogFile>");
                return;
            }
            if (LOG_FILE_PATH.length() > 0) {

                //1、folder用于文件夹存在或者创建文件夹成功
                File folder = new File(LOG_FILE_PATH);
                if (folder.exists() || folder.mkdirs()) {
                    String file = LOG_FILE_PATH + File.separator + TimeUtil.getCurrentTime("yyyy-MM-dd") + ".txt";
//                    String file = LOG_FILE_PATH + File.separator + TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss") + ".txt";

                    //2、logFile用于在上面文件夹路径下创建日志文件
                    File logFile = new File(file);
                    if (logFile.exists() || logFile.createNewFile()) {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write(log + "\n");
                        writer.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}