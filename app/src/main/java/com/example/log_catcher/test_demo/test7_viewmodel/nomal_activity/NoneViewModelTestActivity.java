package com.example.log_catcher.test_demo.test7_viewmodel.nomal_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.log_catcher.R;
import com.example.log_catcher.util.LogHelper;

public class NoneViewModelTestActivity extends AppCompatActivity implements View.OnClickListener {
    //这个变量是根据对应的layout名称而来
    private Button bt_viewmodel_activity_test1, bt_viewmodel_activity_test2;
    private TextView tv_viewmodel_activity_title, tv_viewmodel_activity_num;
    private Context mContext;

    int click_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo7_viewmodel_activity);
        LogHelper.getInstance().e("onCreate!");
        initView();
        initData();

    }

    void initView() {
        tv_viewmodel_activity_title = findViewById(R.id.tv_viewmodel_activity_title);
        tv_viewmodel_activity_num = findViewById(R.id.tv_viewmodel_activity_num);

        //以下几个为调试按钮
        bt_viewmodel_activity_test1 = findViewById(R.id.bt_viewmodel_activity_test1);
        bt_viewmodel_activity_test1.setOnClickListener(this);

        bt_viewmodel_activity_test2 = findViewById(R.id.bt_viewmodel_activity_test2);
        bt_viewmodel_activity_test2.setOnClickListener(this);


    }

    void initData() {

        mContext = NoneViewModelTestActivity.this;

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_viewmodel_activity_test1:
                click_num++;
                tv_viewmodel_activity_num.setText("当前点击量:"+click_num);
                break;
            case R.id.bt_viewmodel_activity_test2:
                click_num++;
                tv_viewmodel_activity_num.setText("当前点击量:"+click_num);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogHelper.getInstance().e("onStart!");
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogHelper.getInstance().e("onResume!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogHelper.getInstance().e("onPause!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogHelper.getInstance().e("onStop!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogHelper.getInstance().e("onDestroy!");
    }
}
