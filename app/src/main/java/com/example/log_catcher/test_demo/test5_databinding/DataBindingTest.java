package com.example.log_catcher.test_demo.test5_databinding;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.log_catcher.R;
import com.example.log_catcher.util.LogHelper;

import androidx.annotation.Nullable;

/**
 *
 * 1、使用DataBinding功能需在build.gradle中添加以下语句:
 * android {
 *
 *      //引入对 DataBinding 的支持
 *     dataBinding {
 *         enabled = true
 *     }
 * }
 *
 * 参考链接：https://www.sohu.com/a/125595511_473801 (基础实例_很好)
 * https://www.imooc.com/article/286709
 */
public class DataBindingTest extends LinearLayout implements View.OnClickListener {

    private Button bt_bind_debug1, bt_bind_debug2;
    private Context mContext;



    private DataBindingListener dataBindingListener;

    //    data class Student(val Name: String, val Age: String)
    public DataBindingTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.demo5_databing, this);
        mContext = context;
        initView();
        initData();
        LogHelper.getInstance().e("XmlJasonParseTest In,attrs=" + attrs);
    }

    public  void setDataBindingListener(DataBindingListener dataBindingListener) {
        this.dataBindingListener = dataBindingListener;
    }

    void initView() {

        bt_bind_debug1 = findViewById(R.id.bt_bind_debug1);
        bt_bind_debug1.setOnClickListener(this);

        bt_bind_debug2 = findViewById(R.id.bt_bind_debug2);
        bt_bind_debug2.setOnClickListener(this);


    }

    void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_bind_debug1:
                LogHelper.getInstance().w("-------------Debug Start------------");
                dataBindingListener.startDataBindingTestAcivity();
                break;
            case R.id.bt_bind_debug2:


                break;
            default:
                break;
        }
    }

    public interface DataBindingListener {
        void startDataBindingTestAcivity();
    }
}
