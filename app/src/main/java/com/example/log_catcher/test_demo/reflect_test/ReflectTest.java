package com.example.log_catcher.test_demo.reflect_test;


import com.example.log_catcher.util.LogHelper;
import com.example.log_catcher.util.TimeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * <p>Description: 验证反射功能<br>
 *   &emsp;&emsp;1、XXXXXXX<br></p>
 * <p>CreatDate: 20200327 <br></p>
 * <p>author: Miles<br></p>
 * <p>version: v1.0<br></p>
 * <p>update: [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]<br></p>
 */
public class ReflectTest {


    //用来开启reflectTest测试
    public static void reflectDebug(){

        //1、获取Class的信息
        LogHelper.getInstance().w("\n\n--------------1、获取Class的信息----------------");
        TimeUtil timeUtil1 = new TimeUtil();
        //it is=class com.example.log_catcher.util.TimeUtil
        LogHelper.getInstance().w("it is="+timeUtil1.getClass());
        //it is=com.example.log_catcher.util
        LogHelper.getInstance().w("it is="+timeUtil1.getClass().getPackage());
        //it is=com.example.log_catcher.util.TimeUtil
        LogHelper.getInstance().w("it is="+timeUtil1.getClass().getName());


        //2、这种静态获取的方法最常用，这个只是实例化了Class的对象，注意不是TimeUtil类的对象;
        LogHelper.getInstance().w("\n\n--------------2、这种静态获取的方法最常用，这个只是实例化了Class的对象，注意不是TimeUtil类的对象;----------------");
        Class<?> c1 = null;
        try {
            c1 = Class.forName("com.example.log_catcher.test_demo.reflect_test.Persion");
            //打印:
            LogHelper.getInstance().w("c1="+c1.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //3、那么怎么实例化Persion类呢？
        //3.1 构造函数无参数的实例化方法
        LogHelper.getInstance().w("\n\n--------------3.1 构造函数无参数的实例化方法----------------");
        Persion persion = null;
        try {
            persion = (Persion)c1.newInstance();
            persion.debug();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //3.2 通过getConstructor函数得到有参构造函数
        LogHelper.getInstance().w("\n\n--------------3.2 通过getConstructor函数得到有参构造函数----------------");
        Persion persion2 = null;
        try {
            Constructor constructor= c1.getConstructor(String.class, int.class);

            //通过构造器对象 newInstance 方法对对象进行初始化 有参数构造函数
            persion2 = (Persion)constructor.newInstance("CoCo", 12);

            Constructor<?> constructors[] = c1.getConstructors();
            //打印如下信息：
            //constructor name[0]=public com.example.log_catcher.test_demo.reflect_test.Persion()
            //constructor name[1]=public com.example.log_catcher.test_demo.reflect_test.Persion(java.lang.String,int)
            for(int i=0; i<constructors.length;i++){
                LogHelper.getInstance().w("constructor name["+i+"]="+constructors[i]);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //4.获取类中的接口类
        LogHelper.getInstance().w("\n\n--------------4.获取类中的接口类----------------");
        try {
            Class<?> interface_classes[] =  c1.getInterfaces();
            for(int i=0; i<interface_classes.length;i++){
                //打印:interface name[0]=interface com.example.log_catcher.test_demo.China
                LogHelper.getInstance().w("interface name["+i+"]="+interface_classes[i]);
                //打印:interface name[0]=com.example.log_catcher.test_demo.China
                LogHelper.getInstance().w("interface name["+i+"]="+interface_classes[i].getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //5.获取父类
        LogHelper.getInstance().w("\n\n--------------5.获取父类----------------");
        try {
            Class<?> super_classes =  c1.getSuperclass();
            LogHelper.getInstance().w("father name="+super_classes.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //6.获取全部方法
        //注意1:需要使用System.out.print打印，因为LogHelper.getInstance().w会换行；
        //注意2：logcat中需要把调试等级调到verbose才能观察的到;
        //--------调试打印信息如下-------------
//        I/System.out: public boolean equals (java.lang.Object arg0)
//        I/System.out: public int getAge ()
//        I/System.out: public final java.lang.Class getClass ()
//        I/System.out: public java.lang.String getName ()
//        I/System.out: public int hashCode ()
//        I/System.out: public final native void notify ()
//        I/System.out: public final native void notifyAll ()
//        I/System.out: public void printfPersonInfo ()
//        I/System.out: public void sayChina ()
//        I/System.out: public java.lang.String sayHello (java.lang.String arg0,int arg1)
//        I/System.out: public void setAge (int arg0)
//        I/System.out: public void setName (java.lang.String arg0)
//        I/System.out: public java.lang.String toString ()
//        I/System.out: public final native void wait () throwsjava.lang.InterruptedException
//        I/System.out: public final void wait (long arg0) throwsjava.lang.InterruptedException
//        I/System.out: public final native void wait (long arg0,int arg1) throwsjava.lang.InterruptedException
        LogHelper.getInstance().w("\n\n--------------6.获取全部方法----------------");
        try {
            Method m[]= c1.getMethods();
            for(int i=0; i<m.length; i++){
                Class<?> r =m[i].getReturnType();
                Class<?> p[] =m[i].getParameterTypes();
                int xx = m[i].getModifiers();
                System.out.print(Modifier.toString(xx)+" ");
                System.out.print(r.getName()+" ");
                System.out.print(m[i].getName()+" (");
                for(int j=0; j<p.length; j++){
                    System.out.print(p[j].getName()+" "+"arg"+j);
                    if(j<(p.length-1)){
                        System.out.print(",");
                    }
                }
                Class<?> exceptions[] =m[i].getExceptionTypes();
                if(exceptions.length>0){
                    System.out.print(") throws");
                }
                else{
                    System.out.print(")");
                }
                for(int k=0; k<exceptions.length; k++){
                    System.out.print(exceptions[k].getName());
                    if(k<(exceptions.length-1)){
                        System.out.print(",");
                    }
                }
                System.out.println("\n");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        //7.获取全部属性
//        I/System.out: 本类属性: private int age  ;
//        I/System.out: 本类属性: public int high  ;
//        I/System.out: 本类属性: private java.lang.String name  ;
        LogHelper.getInstance().w("\n\n--------------7.获取全部属性----------------");
        Field f[] = c1.getDeclaredFields();
        for(int i=0; i<f.length; i++){
            Class<?> r=f[i].getType();
            int mo = f[i].getModifiers();
            String modifyStr = Modifier.toString(mo);
            System.out.print("本类属性: ");
            System.out.print(modifyStr+" ");
            System.out.print(r.getName()+" ");
            System.out.print(f[i].getName()+" ");
            System.out.println(" ;");
        }
        //8、获取公共属性
//        I/System.out: 公共属性: public int high  ;
//        I/System.out: 公共属性: public static final java.lang.String AUTHOR  ;
//        I/System.out: 公共属性: public static final java.lang.String NATIONAL  ;
        LogHelper.getInstance().w("\n\n--------------8、获取公共属性----------------");
        Field public_f[] = c1.getFields();
        for(int i=0; i<public_f.length; i++){
            Class<?> r=public_f[i].getType();
            int mo = public_f[i].getModifiers();
            String modifyStr = Modifier.toString(mo);
            System.out.print("公共属性: ");
            System.out.print(modifyStr+" ");
            System.out.print(r.getName()+" ");
            System.out.print(public_f[i].getName()+" ");
            System.out.println(" ;");
        }
        //9.通过反射调用方法
        LogHelper.getInstance().w("\n\n--------------9.通过反射调用方法----------------");
        Method method1 = null;
        Method method2 = null;
        try {
            method1 = c1.getMethod("sayChina");//此方法没有参数
            method1.invoke(c1.newInstance());//调用此方法，必须传入对象实例

            String ret=null;
            method2 = c1.getMethod("sayHello", String.class, int.class);
            ret = (String)method2.invoke(c1.newInstance(), "LALA", 15);//调用此方法，必须传入对象实例
            LogHelper.getInstance().w(ret);

        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        //10.通过反射设置属性
        LogHelper.getInstance().w("\n\n--------------10.通过反射设置属性----------------");
        Object object = null;
        Field  nameFiled=null;
        Field  ageFiled=null;
        try {
            try {
                object = c1.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            nameFiled = c1.getDeclaredField("name");
            nameFiled.setAccessible(true);

            ageFiled = c1.getDeclaredField("age");
            ageFiled.setAccessible(true);

            try {
                nameFiled.set(object,"LELE");
                ageFiled.set(object,13);
                LogHelper.getInstance().w("name="+nameFiled.get(object));
                LogHelper.getInstance().w("age="+ageFiled.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } catch (NoSuchFieldException e){
            e.printStackTrace();
        }
        //11、动态代理
        Persion stu = new Persion();
        //11.1确认classLoaderdalvik.system.PathClassLoader
        LogHelper.getInstance().w(stu.getClass().getClassLoader().getClass().getName());
        //11.2动态代理测试
        MyInvocationHandler handler = new MyInvocationHandler();
        Subject subject = (Subject) handler.bind(new RealSubject());
        String info = subject.say("CHEHCE",12);
        LogHelper.getInstance().w(info);
    }


}
