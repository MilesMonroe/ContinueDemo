package com.example.log_catcher.test_demo.xml_json_test;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.log_catcher.R;
import com.example.log_catcher.test_demo.xml_json_test.json.FastJsonTest;
import com.example.log_catcher.test_demo.xml_json_test.xml.XmlSaxHandlerHelper;
import com.example.log_catcher.util.FileUtils;
import com.example.log_catcher.util.LogHelper;
//import com.google.gson.Gson;


import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;

public class XmlJasonParseTest extends LinearLayout implements View.OnClickListener{

    private Button bt_xml_parse,bt_json_parse;
    private Handler mHandler;
    private Context mContext;
    private String  FilePath;


    public XmlJasonParseTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout4_xml_json,this);
        mContext = context;
        initView();
        initData();
        LogHelper.getInstance().e("XmlJasonParseTest In,attrs="+attrs);
    }

    void initView(){

        bt_xml_parse = findViewById(R.id.bt_xml_parse);
        bt_xml_parse.setOnClickListener(this);

        bt_json_parse = findViewById(R.id.bt_json_parse);
        bt_json_parse.setOnClickListener(this);
    }

    void initData(){

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_xml_parse:
                LogHelper.getInstance().w("-------------Debug Start------------");
                xmlParseDebug();
                break;
            case R.id.bt_json_parse:
                LogHelper.getInstance().w("-------------Debug Start------------");
                jsonParseDebug();
                break;
            default:
                break;
        }
    }
    public  void xmlParseDebug(){
        String FilePathSet=null;
        FilePathSet = this.FilePath;


        Bundle bundle = new Bundle();
        if((FilePathSet == null)||(!FileUtils.isSpecifiedFileType(FilePathSet,"xml"))){
            bundle.putString("Toast", "请选择xml文件!");
            showHandlerMsg("Toast",bundle);
            return;
        }

        //5、XML文件解析
        XmlSaxHandlerHelper.parseXmlBySax(new File(FilePathSet));
    }


    public  void jsonParseDebug(){

        FastJsonTest fastJsonTest = new FastJsonTest();
        LogHelper.getInstance().w("--------json test start---------");
        LogHelper.getInstance().w("--------objectToJson:");
        fastJsonTest.objectToJson();
        LogHelper.getInstance().w("--------JsonToObject:");
        fastJsonTest.JsonToObject();

        //=================json file parse===============
        String FilePathSet=null;
        FilePathSet = this.FilePath;

        Bundle bundle = new Bundle();
        if((FilePathSet == null)||(!FileUtils.isSpecifiedFileType(FilePathSet,"json"))){
            bundle.putString("Toast", "请选择json文件!");
            showHandlerMsg("Toast",bundle);
            return;
        }
        try {
            fastJsonTest.parserJsonTxt(new File(FilePathSet));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogHelper.getInstance().w("--------json test end---------");
    }

    //提示信息
    public void showHandlerMsg(String operation, Bundle bundle) {
        Message msg = new Message();
        msg.obj = operation;
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    //setter/getter
    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }
}
