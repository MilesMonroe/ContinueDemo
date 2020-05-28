package com.example.log_catcher.test_demo.test1_reflect;
import com.example.log_catcher.util.LogHelper;

interface China{
    public static final String NATIONAL = "China";
    public static final String AUTHOR = "Miles";
    public void sayChina();
    public String sayHello(String name, int age);

}

public class Persion implements China{

    private String name = "baby";
    private int age = 1;
    public int high = 172;

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

    public void printfPersonInfo(){
        LogHelper.getInstance().w("name="+name);
    }



    /*******************************反射test***************************/
    public void debug(){
        LogHelper.getInstance().w("reflectDebug");
    }


    @Override
    public void sayChina() {
        LogHelper.getInstance().w("作者:"+AUTHOR+",国籍:"+NATIONAL);
    }

    @Override
    public String sayHello(String name, int age) {
        return name+":Hello，I am"+age;
    }
}
