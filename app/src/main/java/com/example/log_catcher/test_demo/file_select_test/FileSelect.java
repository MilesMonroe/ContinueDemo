package com.example.log_catcher.test_demo.file_select_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duke.dfileselector.activity.DefaultSelectorActivity;
import com.duke.dfileselector.util.FileSelectorUtils;
import com.example.log_catcher.R;
import com.example.log_catcher.util.LogHelper;

import java.util.ArrayList;

import androidx.annotation.Nullable;




/**本类功能基于module<DFileSelector>实现:
 * 1、
 *
 * 2、
 *
 *
 */
public class FileSelect extends LinearLayout implements View.OnClickListener {

    private Button bt_file_sel;
    private Context mContext;

    public FileSelect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout3_file_select,this);
        mContext = context;
        initView();
        initData();
    }

    void initView(){

        bt_file_sel = findViewById(R.id.bt_file_sel);
        bt_file_sel.setOnClickListener(this);
    }

    void initData(){

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_file_sel:
                LogHelper.getInstance().w("-------------fileSelect Start------------");
                fileSelect();
                break;

            default:
                break;
        }
    }


    public void fileSelect(){

        DefaultSelectorActivity.startActivity(mContext);//包含广播
    }


}
