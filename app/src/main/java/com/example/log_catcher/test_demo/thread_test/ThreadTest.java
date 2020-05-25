package com.example.log_catcher.test_demo.thread_test;

import com.example.log_catcher.util.LogHelper;



/**
 * <p>Description: 验证线程功能<br>
 *   &emsp;&emsp;1、XXXXXXX<br></p>
 * <p>CreatDate: 20200402 <br></p>
 * <p>author: Miles<br></p>
 * <p>version: v1.0<br></p>
 * <p>update: [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]<br></p>
 */
public class ThreadTest {


    //本利用于说明继承Runnable接口的线程调用方式比起继承Thread类的区别
    //和Runnable接口方式的使用
    public static void threadDebug1(){

        //<Thread-2>: aa-Thread-2--卖票:ticket=5
        //<Thread-3>: aa-Thread-3--卖票:ticket=4
        //<Thread-4>: aa-Thread-4--卖票:ticket=3
        //<Thread-2>: aa-Thread-2--卖票:ticket=2
        //<Thread-3>: aa-Thread-3--卖票:ticket=1
        MyThreadRunable myRunable1= new MyThreadRunable("aa");
        Thread my1 = new Thread(myRunable1);
        Thread my2 = new Thread(myRunable1);
        Thread my3 = new Thread(myRunable1);
        my1.start();
        my2.start();
        my3.start();
    }

    //本例用于验证以下两点:
    //1、对线程同步(synchronized)：用于对信息名称和内容的同步;
    //2、等待(wait)/唤醒(notify)的使用：用于生产1条/取1条的原则取出;
    public static void threadDebug2(){
        InfoForThreadTest info = new InfoForThreadTest();
        Producer producer = new Producer(info);
        Consumer consumer = new Consumer(info);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}


/**
 * 这种继承Runnable接口的线程调用方式比起继承Thread类的优势：<br>
 *         MyThreadRunable myRunable1= new MyThreadRunable("aa");
 *         Thread my1 = new Thread(myRunable1);
 *         Thread my2 = new Thread(myRunable1);
 *         Thread my3 = new Thread(myRunable1);
 *         my1.start();
 *         my2.start();
 *         my3.start();
 *    1、因为以上my1\my2\my3都是通过Thread对同一个myRunable1进行操作，
 *      所以多个线程对MyThreadRunable中的属性可共用<br>
 *
 */
class MyThreadRunable implements Runnable{

    private int ticket=5;
    public String name;

    public MyThreadRunable(String name) {
        this.name = name;
        LogHelper.getInstance().w("-----"+this.getClass().getName());
    }

    public void run() {

        for (int i = 0; i < 5; i++) {
            if (ticket > 0) {
                LogHelper.getInstance().w(name + "-" + Thread.currentThread().getName() + "-" + "-卖票:ticket=" + ticket--);
            }

        }
    }
}

/**
 * 本类用于说明生产者/消费者模式时的同步
 */
class  Producer implements Runnable{
    private InfoForThreadTest info = new InfoForThreadTest();

    public Producer(InfoForThreadTest info) {
        this.info = info;
    }

    @Override
    public void run() {
        boolean flag = false;
        for(int i=0; i<10; i++){
            if(flag){
//                this.info.setName("李兴华");
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                this.info.setContent("JAVA讲师");
                this.info.set("李兴华", "JAVA讲师");
                flag = false;
            }
            else{
//                this.info.setName("mldn");
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                this.info.setContent("www.mldn.com");
                this.info.set("mldn", "www.mldn.com");
                flag = true;
            }
        }

    }
}


/**
 * 本类用于说明生产者/消费者模式时的同步
 */
class Consumer implements Runnable{
    private InfoForThreadTest info = new InfoForThreadTest();

    public Consumer(InfoForThreadTest info) {
        this.info = info;
    }

    @Override
    public void run() {
        for(int i=0; i<10; i++){

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            LogHelper.getInstance().w(this.info.getName()+"--->"+this.info.getContent());
            this.info.get();
        }

    }

}

