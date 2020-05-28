package com.example.log_catcher.test_demo.test1_reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler {
    private Object object = null; //真实主题
    public Object bind(Object obj){ //绑定真实操作主题
            this.object = obj;

        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces(),this); //取得代理对象
    }

    @Override
    ////ret = (String)method2.invoke(c1.newInstance(), "LALA", 15);//调用此方法，必须传入对象实例
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object temp = method.invoke(this.object, args);
        return temp;
    }
}

interface Subject{
    public String say(String name, int age);
}

class RealSubject implements Subject{

    @Override
    public String say(String name, int age) {
        return "姓名:"+name+",年龄:"+age;
    }
}