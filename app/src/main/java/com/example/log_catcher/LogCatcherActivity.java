package com.example.log_catcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duke.dfileselector.activity.DefaultSelectorActivity;
import com.duke.dfileselector.util.FileSelectorUtils;
import com.example.log_catcher.service.LogCatcherService;

import com.example.log_catcher.test_demo.test5_databinding.DataBindingTest;
import com.example.log_catcher.test_demo.test5_databinding.test_activity.TestActivity;
import com.example.log_catcher.test_demo.test7_viewmodel.nomal_activity.NoneViewModelTestActivity;

import com.example.log_catcher.test_demo.test4_xml_json.XmlJasonParseTest;
import com.example.log_catcher.test_demo.test7_viewmodel.ViewModelTest;
import com.example.log_catcher.test_demo.test7_viewmodel.viewmodel_actiity.NormalViewModelTestActivity;
import com.example.log_catcher.util.ERROR;
import com.example.log_catcher.util.LogHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
//import android.support.v4.content.FileProvider;

public class LogCatcherActivity extends AppCompatActivity implements View.OnClickListener, DataBindingTest.DataBindingListener
                                                            , ViewModelTest.ViewModelTestListener {
    private final String TAG = "Miles";
    private Button bt_start_service;
    private Button bt_stop_service;
    private Button bt_bind_service;
    private Button bt_unbind_service;
    private Button bt_debug;
    private TextView textView;
    private EditText ed_now_time,ed_showfilepath;
    private boolean time_run_flag = false;
    int time_thread_count = 0;
    SimpleDateFormat simpleDateFormat;
    int count = 0;
    private Context mContext;
    private LogCatcherService.Downloadbinder downloadbinder;
    private String selectFilePath = null;
    XmlJasonParseTest xmlJasonParseTest;
    DataBindingTest dataBindingTest;
    ViewModelTest viewModelTest;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogHelper.getInstance().w("onServiceConnected");
            downloadbinder = (LogCatcherService.Downloadbinder) service;
            downloadbinder.startDownload();
            downloadbinder.getProgress();
            downloadbinder.startThread();
        }

        //1、onServiceDisconnected() 在连接正常关闭的情况下是不会被调用的.
//        2、该方法只在Service 被破坏了或者被杀死的时候调用.
//        例如, 系统资源不足, 要关闭一些Services, 刚好连接绑定的 Service 是被关闭者之一,
//         这个时候onServiceDisconnected() 就会被调用.

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogHelper.getInstance().w("onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_catcher);

        initView();
        initData();

    }

    void initView() {
        textView = findViewById(R.id.sample_text);
        ed_now_time = findViewById(R.id.ed_now_time);
        ed_showfilepath = findViewById(R.id.ed_showfilepath);

        //以下几个为调试按钮
        bt_start_service = findViewById(R.id.bt_start_service);
        bt_start_service.setOnClickListener(this);

        bt_stop_service = findViewById(R.id.bt_stop_service);
        bt_stop_service.setOnClickListener(this);

        bt_bind_service = findViewById(R.id.bt_bind_service);
        bt_bind_service.setOnClickListener(this);

        bt_unbind_service = findViewById(R.id.bt_unbind_service);
        bt_unbind_service.setOnClickListener(this);

        bt_debug = findViewById(R.id.bt_debug);
        bt_debug.setOnClickListener(this);

        //================xml调试布局===================
        xmlJasonParseTest = findViewById(R.id.layout_file_select);
        dataBindingTest = findViewById(R.id.databinding_test);
        viewModelTest = findViewById(R.id.view_model_test);

    }

    void initData() {
        requestPermission();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        mContext = LogCatcherActivity.this;

        //--------获取包名"it is =com.example.log_catcher"------
        LogHelper.getInstance().e("it is =" + mContext.getPackageName());

        //--------获取separator符号，android下为"/"------
        LogHelper.getInstance().e("---------------=" + File.separator);

        //--------结论:trim()去掉了头尾小于空格（十进制32）的ASCII码------
        String a = "  2  34  ";
        String b;
        b = a.trim();
        LogHelper.getInstance().w("222b=" + b);



//        //启动时间刷新
        time_run_flag = true;
        new TimeThread().start();
//        System.out.print("123XXXXXXXXXXXXX");

        //--------------反射Demo-----------------
        //ReflectTest.reflectDebug();

        //--------------ThreadDemo-----------------
        //ThreadTest.threadDebug1();
        //ThreadTest.threadDebug2();

        //--------------demo4 xml_json_Demo-----------------
        xmlJasonParseTest.setContext(mContext);
        xmlJasonParseTest.setHandler(mHandler);
        xmlJasonParseTest.setFilePath(selectFilePath);

        //--------------demo5 DataBindingDemo-----------------
        dataBindingTest.setDataBindingListener(LogCatcherActivity.this);

        //--------------demo7 viewModelTest-----------------
        viewModelTest.setViewModelTestListener(LogCatcherActivity.this);
    }

    //提示信息
    public void showHandlerMsg(String operation, Bundle bundle) {
        Message msg = new Message();
        msg.obj = operation;
        msg.setData(bundle);
        mHandler.sendMessage(msg);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            Date date = new Date();
            String timeStr = simpleDateFormat.format(date);
            switch ((String) msg.obj) {

                case "time msg":

                    //Time刷新
//                    textView.setText("it is: "+count);
//                    LogHelper.getInstance().w("11111111111date="+date+",timeStr="+timeStr);
                    ed_now_time.setText(timeStr);
                    break;

                case "filepath":
                    String filepath;
                    filepath = bundle.getString("filepath");
                    filepath = filepath.trim();
                    xmlJasonParseTest.setFilePath(filepath);
                    ed_showfilepath.setText(filepath);
                    break;

                case "Toast":
                    String toastMsg = bundle.getString("Toast");
                    Toast.makeText(mContext, toastMsg, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_start_service:
                Intent startIntent = new Intent(this, LogCatcherService.class);
                startService(startIntent);
                break;

            case R.id.bt_stop_service:
                Intent stopIntent = new Intent(this, LogCatcherService.class);
                stopService(stopIntent);
                break;

            case R.id.bt_bind_service:
                Intent bindIntent = new Intent(this, LogCatcherService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;

            case R.id.bt_unbind_service:
                unbindService(connection);
                break;

            case R.id.bt_debug:
//                LogHelper.getInstance().w("get root="+LinuxCmdUtil.requestRoot());
                //创建本地文件读写文件
//                LogCatcher_system_debug.apk
//                String filePath = Environment.getExternalStorageDirectory().toString()+"/Download/IssueTool_V1.00.apk";
                String apkFilePath = Environment.getExternalStorageDirectory().toString() + "/Download/LogCatcher_system_debug.apk";
                LogHelper.getInstance().w("path:" + Environment.getExternalStorageDirectory() + "/Download");
                LogHelper.getInstance().w("path:" + apkFilePath);
//                File firmwareFile= new File(filePath);
//                ApkUtils.installAPK(mContext,apkFilePath);
                LogHelper.getInstance().w("Capacity=" + getCurrentCapacity());
//                installSilenceApp(apkFilePath);
//                callAnotherApk(mContext, "com.example.myapplication", "com.example.myapplication.MainActivity",2);
//                startApp(mContext);
//                FileUtils.deleteDirectory(Environment.getExternalStorageDirectory().toString()+"/Download/1");
                break;

            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
//        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_log_level:
                LogHelper.getInstance().w("menu_log_level");
                return true;
            case R.id.menu_open_log:
                LogHelper.getInstance().w("menu_open_log");

                return true;
//            case R.id.menu_save_log:
//            case R.id.menu_save_as_log:
//                showSaveLogDialog();
//                return true;

        }
        return false;
    }

    //开启这个线程的前提是USB已连接
    class TimeThread extends Thread {
        @Override
        public void run() {

            Bundle bundle = new Bundle();
            do {
                try {
                    Thread.sleep(1000);
//                    LogHelper.getInstance().w("TimeThread printf");
                    bundle.putString("time msg", "do nothing");
                    showHandlerMsg("time msg", bundle);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (time_run_flag);

            LogHelper.getInstance().e("time_thread_count end:thread_count--");
            time_thread_count--;
            LogHelper.getInstance().e("time_thread_count end:thread_count=" + time_thread_count);

        }
    }


    /**
     * 获取当前电量
     * <p>
     * adb shell "cat /sys/class/power_supply/battery/capacity"
     */
    private int getCurrentCapacity() {
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

    //==================================demo5 DataBinding测试所需的接口实现============================================
    @Override
    public void startDataBindingTestAcivity(){
        Intent intent = new Intent(mContext, TestActivity.class);
        startActivity(intent);
    }

    //==================================demo7 ViewModel测试所需的接口实现============================================
    @Override
    public void startNoneViewModelTestAcivity(){
        Intent intent = new Intent(mContext, NoneViewModelTestActivity.class);
        startActivity(intent);
    }
    @Override
    public void startNormalViewModelTestAcivity(){
        Intent intent = new Intent(mContext, NormalViewModelTestActivity.class);
        startActivity(intent);
    }


    //==================================文件权限============================================
    void requestPermission() {
        //动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LogHelper.getInstance().w("requestPermission111");
            // Android M Permission check
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                LogHelper.getInstance().w("requestPermission222");
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 0x01);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w(TAG, "申请的权限回调函数被触发！");
        switch (requestCode) {
            case 1:
                for (int i = 0; i < permissions.length; i++) {
                    Log.w(TAG, "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
                }
                break;
            case 2:

                break;
            default:
                break;
        }
    }


    //=================================文件选择相关的操作（接收到广播后获取路径）========================
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            if (DefaultSelectorActivity.FILE_SELECT_ACTION.equals(intent.getAction())) {

                //文件单选
                selectFilePath = printData(DefaultSelectorActivity.getDataFromIntent(intent));
                if (selectFilePath != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("filepath", selectFilePath);
                    //刷新界面
                    showHandlerMsg("filepath", bundle);
                }

            }
        }
    };


    private boolean isRegister = false;
    private IntentFilter intentFilter = new IntentFilter(DefaultSelectorActivity.FILE_SELECT_ACTION);


    private String printData(ArrayList<String> list) {
        if (FileSelectorUtils.isEmpty(list)) {
            return null;
        }
        int size = list.size();

        LogHelper.getInstance().w("--->获取到数据-开始 size = " + size);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            LogHelper.getInstance().w((i + 1) + ">>" + list.get(i)+"<<");
            stringBuffer.append(list.get(i));
            stringBuffer.append("\r\n");
        }
        LogHelper.getInstance().w("--->获取到数据-结束");
        return stringBuffer.toString();
    }

    protected void onResume() {
        super.onResume();
        if (!isRegister) {
            registerReceiver(receiver, intentFilter);
            isRegister = true;
        }
    }

    @Override
    protected void onDestroy() {
        LogHelper.getInstance().w("onDestroy in");
        //时间刷新
        time_run_flag = false;

        //文件夹广播注销
        if (isRegister) {
            unregisterReceiver(receiver);
            isRegister = false;
        }
        super.onDestroy();
    }

//==============================正则表达式===========================================
    /**设置BLE模式的MAC地址;<br>
     *
     * @param MacAddr 设置的MAC地址，要求为12个字符即可，无需添加:或-间隔符<br>
     * @return 0:成功<br>
     *         !0:失败<br>
     */
    public int setBLEMac(String MacAddr) {

        int ret = -1;
        byte[] info = new byte[100];
        String recvInfo = null;

        if (MacAddr == null) {
            return ERROR.ERR_FAIL;
        }

        String patternMac = "^([a-fA-F0-9]{2}){6}$";
        Pattern pa = Pattern.compile(patternMac);
        boolean isMac = pa.matcher(MacAddr).find();
        if (!isMac) {
            LogHelper.getInstance().w("MacAddr=" + MacAddr + "invalid");
            return ERROR.ERR_PARAM;
        }

        return ERROR.SUCCESS;
    }

}

