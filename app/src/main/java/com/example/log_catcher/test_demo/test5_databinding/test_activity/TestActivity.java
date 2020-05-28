package com.example.log_catcher.test_demo.test5_databinding.test_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.log_catcher.R;
import com.example.log_catcher.databinding.Demo5DatabingActivityBinding;
import com.example.log_catcher.util.LogHelper;

public class TestActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    //这个变量是根据对应的layout名称而来
    private Demo5DatabingActivityBinding binding;
    private Button bt_bind_activity_debug1, bt_bind_activity_debug2;
    private Context mContext;
    private Student student;
    private UserObservable userObservable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = TestActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.demo5_databing_activity);

        //1、通过binding设置变量名称，从而达到在layout中同步更改的效果;省去findViewById(R.id.xxx)和赋值操作
        student = new Student("HoHo", 24);
        binding.setStudent(student);
        binding.setBt1Name("按钮1");
        binding.setBt2Name("按钮2");

        //2、通过binding.tvTitle（layout中的id）直接在本应用层调用;省去findViewById(R.id.xxx)和赋值操作；
        //binding.tvTitle.setText("新的debug名称");

        //3、注意已在layout中声明了main变量，所以这里进行了绑定而已；从而达到在layout的button中响应android:onClick事件
        binding.setMain(this);

        //4、通过ObservableFields方式实现（非继承BaseObservable的方式）
        userObservable = new UserObservable("JoJo",25);
        binding.setUserObservable(userObservable);
    }

//由DataBinding代替了
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.bt_bind_activity_debug1:
//                LogHelper.getInstance().w("-------------Debug Start------------");
//
//                break;
//            case R.id.bt_bind_activity_debug2:
//
//                break;
//            default:
//                break;
//        }
//    }

    public void clickHandler01(View v){
        LogHelper.getInstance().w("clickHandler01");
        Toast.makeText(mContext,"检测到点击事件",Toast.LENGTH_SHORT).show();
        student.setName("WoWo");
        //如果想要去掉以下这步，只更改student.setName("WoWo")就实现UI刷新的效果，需要加入BaseObservable模式
        //具体实现方法，见Student类的注释
//        binding.setStudent(student);
    }

    public void clickHandler02(View v){
        LogHelper.getInstance().w("clickHandler02");
        Toast.makeText(mContext,"检测到点击事件",Toast.LENGTH_SHORT).show();
        userObservable.setName("LoLo");
        userObservable.setPassword(236);

    }
}
