package com.example.log_catcher.test_demo.test3_file_select;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.duke.dfileselector.activity.DefaultSelectorActivity;
import com.example.log_catcher.R;
import com.example.log_catcher.util.LogHelper;

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
        LayoutInflater.from(context).inflate(R.layout.demo3_file_select,this);
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
