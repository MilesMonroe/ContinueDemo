package com.example.log_catcher.test_demo.test5_databinding.test_activity;

import androidx.databinding.ObservableField;

/**
 * 省去了Student中的以下几步:
 * 1、本类继承BaseObservable
 * 2、先看看getter方法，所有的getter方法都添加了一个注解@Bindable，它支持该属性在BR类中产生一个对应的静态int类型常量(至于什么是BR，你可以认为它类似于我们认识的R类)；
 * 3、另一个变化就是setter方法中多了一句notifyPropertyChanged(BR.xxx);代码，该代码用来通知刷新当前显示的那个布局：
 */
public class UserObservable {
    public  ObservableField<String> name = new ObservableField<>();
    public  ObservableField<Integer> password = new ObservableField<>();
    public UserObservable(String name, int password) {
        this.name = new ObservableField<>(name);
        this.password = new ObservableField<>(password);
    }

    public ObservableField<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObservableField<Integer> getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password.set(password);
    }
}
