package com.example.log_catcher.test_demo.test6_rxjava_retrofit;

import com.example.log_catcher.util.LogHelper;

public class Persion extends ABC_Test {

    private String name = "baby";
    private int age = 1;

    public Persion() {
        LogHelper.getInstance().w("it is Persion, no param");
    }
    public Persion(String name, int age) {
        this.name = name;
        this.age = age;
        LogHelper.getInstance().w("Persion, name="+name+", age="+age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void printf() {
        LogHelper.getInstance().w("it is ABS_Test = printf()");
    }

    @Override
    public void printf2() {
        LogHelper.getInstance().w("it is ABS_Test = printf2()");
    }
}
