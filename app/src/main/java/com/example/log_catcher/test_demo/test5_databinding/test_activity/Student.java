package com.example.log_catcher.test_demo.test5_databinding.test_activity;

import com.example.log_catcher.BR;
import androidx.databinding.Bindable;
import androidx.databinding.BaseObservable;

/**
 * 期望:更改Student的name就同步刷新到对应的UI上。
 * 实现需要以下几步：
 * 1、本类继承BaseObservable
 * 2、先看看getter方法，所有的getter方法都添加了一个注解@Bindable，它支持该属性在BR类中产生一个对应的静态int类型常量(至于什么是BR，你可以认为它类似于我们认识的R类)；
 * 3、另一个变化就是setter方法中多了一句notifyPropertyChanged(BR.xxx);代码，该代码用来通知刷新当前显示的那个布局：
 */
public class Student  extends BaseObservable {
    String name = "CoCo";
    int age = 10;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }
}
