package com.example.log_catcher.test_demo.test7_viewmodel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.log_catcher.R;
import com.example.log_catcher.test_demo.test7_viewmodel.lambd_test.Lambda_Test;
import com.example.log_catcher.util.LogHelper;

import androidx.annotation.Nullable;

public class ViewModelTest extends LinearLayout implements View.OnClickListener {

    private Button bt_viewmodel_test1, bt_viewmodel_test2, bt_lambda_test, bt_viewmodel_test4;
    private Context mContext;

    private  ViewModelTestListener viewModelTestListener;

    //    data class Student(val Name: String, val Age: String)
    public ViewModelTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.demo7_view_model, this);
        mContext = context;
        initView();
        initData();
        LogHelper.getInstance().e("XmlJasonParseTest In,attrs=" + attrs);
    }

    public  void setViewModelTestListener(ViewModelTestListener viewModelTestListener) {
        this.viewModelTestListener = viewModelTestListener;
    }

    void initView() {

        bt_viewmodel_test1 = findViewById(R.id.bt_viewmodel_test1);
        bt_viewmodel_test1.setOnClickListener(this);

        bt_viewmodel_test2 = findViewById(R.id.bt_viewmodel_test2);
        bt_viewmodel_test2.setOnClickListener(this);

        bt_lambda_test = findViewById(R.id.bt_lambda_test);
        bt_lambda_test.setOnClickListener(this);

    }

    void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_viewmodel_test1:
                LogHelper.getInstance().w("-------------Debug Start------------");
                //旋转后，按钮点击数会清零
                viewModelTestListener.startNoneViewModelTestAcivity();
                break;
            case R.id.bt_viewmodel_test2:
                LogHelper.getInstance().w("-------------Debug Start------------");
                //旋转后，按钮点击数不会清零
                viewModelTestListener.startNormalViewModelTestAcivity();

                break;

            case R.id.bt_lambda_test:
                LogHelper.getInstance().w("-------------Debug Start------------");
                //旋转后，按钮点击数不会清零
                Lambda_Test.do_test();
                break;
            default:
                break;
        }
    }

    public interface ViewModelTestListener {
        void startNoneViewModelTestAcivity();
        void startNormalViewModelTestAcivity();
    }
}