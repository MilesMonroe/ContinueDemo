package com.example.log_catcher.test_demo.test1_thread;

import com.example.log_catcher.util.LogHelper;

public class InfoForThreadTest {
    private String name = "李兴华";
    private String content = "JAVA讲师";
    private boolean producingFlag = true; //true：只能生产，但不能取; false；只能消费，但不能生产

    public synchronized void set(String name, String content){
        if(producingFlag==false){//只能消费,不能生产，所以等待
            try {
                super.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setName(name);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setContent(content);
        producingFlag = false;
        super.notify();
    }

    public synchronized void get() {
        if(producingFlag==true){ //只能生产,不能消费，所以等待
            try {
                super.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LogHelper.getInstance().w(this.getName() + "--->" + this.getContent());
        producingFlag = true;
        super.notify();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
