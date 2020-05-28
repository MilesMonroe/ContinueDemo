package com.example.log_catcher.test_demo.test6_rxjava_retrofit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.log_catcher.R;
import com.example.log_catcher.util.LogHelper;

import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

//import com.google.gson.Gson;

/**======================RxJava=======================
 * 1、RxJava需在gradle中加入以下依赖
 *     implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
 *     implementation 'io.reactivex.rxjava2:rxjava:2.2.19'
 * 2、参考网址:（原理+基本使用）https://blog.csdn.net/carson_ho/article/details/78179340
 *           (实例进阶) https://www.jianshu.com/p/d149043d103a
 *
 * ====================Retrofit=======================
 * 1、RxJava需在gradle中加入以下依赖
 *      //Retrofit
 *     implementation 'com.squareup.retrofit2:retrofit:2.3.0'
 *      //Gson converter
 *     implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
 *      //RxJava　adapter
 *     implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
 *
 * 2、参考网址:
 */
public class RxJavaRetrofitTest extends LinearLayout implements View.OnClickListener{

    private Button bt_rxjava_test1,bt_rxjava_test2,bt_rxjava_test3,bt_rxjava_test4,
                   bt_rxjava_test5,bt_rxjava_test6,bt_rxjava_test7,bt_rxjava_test8,
                   bt_retrofit_test1,bt_retrofit_test2;
    private Handler mHandler;
    private Context mContext;

    public RxJavaRetrofitTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.demo6_rxjava,this);
        mContext = context;
        initView();
        initData();
        LogHelper.getInstance().e("XmlJasonParseTest In,attrs="+attrs);
    }

    void initView(){

        bt_rxjava_test1 = findViewById(R.id.bt_rxjava_test1);
        bt_rxjava_test1.setOnClickListener(this);

        bt_rxjava_test2 = findViewById(R.id.bt_rxjava_test2);
        bt_rxjava_test2.setOnClickListener(this);

        bt_rxjava_test3 = findViewById(R.id.bt_rxjava_test3);
        bt_rxjava_test3.setOnClickListener(this);

        bt_rxjava_test4 = findViewById(R.id.bt_rxjava_test4);
        bt_rxjava_test4.setOnClickListener(this);

        bt_rxjava_test5 = findViewById(R.id.bt_rxjava_test5);
        bt_rxjava_test5.setOnClickListener(this);

        bt_rxjava_test6 = findViewById(R.id.bt_rxjava_test6);
        bt_rxjava_test6.setOnClickListener(this);

        bt_rxjava_test7 = findViewById(R.id.bt_rxjava_test7);
        bt_rxjava_test7.setOnClickListener(this);

        bt_rxjava_test8 = findViewById(R.id.bt_rxjava_test8);
        bt_rxjava_test8.setOnClickListener(this);

        bt_retrofit_test1 = findViewById(R.id.bt_retrofit_test1);
        bt_retrofit_test1.setOnClickListener(this);

        bt_retrofit_test2 = findViewById(R.id.bt_retrofit_test2);
        bt_retrofit_test2.setOnClickListener(this);


    }

    void initData(){

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_rxjava_test1:
                LogHelper.getInstance().w("-------------Debug Start1------------");
                RxJavaDebug();
                break;
            case R.id.bt_rxjava_test2:
                LogHelper.getInstance().w("-------------Debug Start2------------");
                RxJavaDebugByChained();
                break;
            case R.id.bt_rxjava_test3:
                LogHelper.getInstance().w("-------------Debug Start3------------");
                RxJavaDebugByChainedSimple();
                break;
            case R.id.bt_rxjava_test4:
                LogHelper.getInstance().w("-------------Debug Start4------------");
                RxJavaDebugMap();
                break;
            case R.id.bt_rxjava_test5:
                LogHelper.getInstance().w("-------------Debug Start5------------");
                RxJavaDebugFlatMap();
                break;
            case R.id.bt_rxjava_test6:
                LogHelper.getInstance().w("-------------Debug Start6------------");
                RxJavaDebugFlatMapUsedKeyword();
                break;
            case R.id.bt_rxjava_test7:
                LogHelper.getInstance().w("-------------Debug Start7------------");
                RxJavaDebugScheduler();
                break;
            case R.id.bt_rxjava_test8:
                LogHelper.getInstance().w("-------------Debug Start8------------");
//                RxJavaDebugFlatMapUsedKeyword();
                break;
            case R.id.bt_retrofit_test1:
                LogHelper.getInstance().w("-------------bt_retrofit_test1------------");
//                RxJavaDebugFlatMapUsedKeyword();
                break;
            case R.id.bt_retrofit_test2:
                LogHelper.getInstance().w("-------------bt_retrofit_test2------------");
//                RxJavaDebugFlatMapUsedKeyword();
                break;

            default:
                break;
        }
    }

    /**示例1、RxJava的基本接口使用验证
     *
     */
    public  void RxJavaDebug(){
        //1、创建一个观察者(方式1：采用Observer 接口)，用于决定事件触发的时候将有怎样的行为
        //另外一种方式2 Subscriber<String> subscriber= new Subscriber<Integer>()这里不做演示，
        // 参考网址https://blog.csdn.net/carson_ho/article/details/78179340
        Observer<String> observer = new Observer<String>() {

            // 2. 创建对象时通过对应复写对应事件方法 从而 响应对应事件
            // 2.1 观察者接收事件前，默认最先调用复写 onSubscribe（）
            @Override
            public void onSubscribe(Disposable d) {
                LogHelper.getInstance().w("开始采用subscribe连接");
            }

            // 2.2 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(String s) {
                LogHelper.getInstance().w("对Next事件作出响应" + s);
            }

            // 2.3 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {
                LogHelper.getInstance().w("对Error事件作出响应");
            }

            // 2.4 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {
                LogHelper.getInstance().w("对Complete事件作出响应");
            }

        };
        //2、创建被观察者，(方式1、使用Observable.create())，用于决定什么时候触发事件以及触发怎样的事件
        Observable observable1 =Observable.create(new ObservableOnSubscribe<String>() {
            // create() 是 RxJava 最基本的创造事件序列的方法
            // 此处传入了一个 OnSubscribe 对象参数
            // 当 Observable 被订阅时，OnSubscribe 的 call() 方法会自动被调用，即事件序列就会依照设定依次被触发
            // 即观察者会依次调用对应事件的复写方法从而响应事件
            // 从而实现被观察者调用了观察者的回调方法 & 由被观察者向观察者的事件传递，即观察者模式

            // 2.1 在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 通过 ObservableEmitter类对象产生事件并通知观察者
                // ObservableEmitter类介绍
                // a. 定义：事件发射器
                // b. 作用：定义需要发送的事件 & 向观察者发送事件
                emitter.onNext("Observable emit event1");
                emitter.onNext("Observable emit event2");
                emitter.onNext("Observable emit event3");
                emitter.onComplete();
            }
        });

        //<--扩展：RxJava 提供了其他方法用于 创建被观察者对象Observable -->
        //// (方式2、使用just(T...)：直接将传入的参数依次发送出来）
        //  Observable observable = Observable.just("A", "B", "C");
        //  // 上述接口将会依次调用：
        //  // onNext("A");
        //  // onNext("B");
        //  // onNext("C");
        //  // onCompleted();
        //
        //// (方式3、：from(T[]) / from(Iterable<? extends T>) : 将传入的数组 / Iterable 拆分成具体对象后，依次发送出来）
        //  String[] words = {"A", "B", "C"};
        //  Observable observable = Observable.from(words);
        //  // 上述接口将会依次调用：
        //  // onNext("A");
        //  // onNext("B");
        //  // onNext("C");
        //  // onCompleted();


        //3、订阅:创建了Observable和Observer之后，再用subscribe()方法将它们联结起来
        observable1.subscribe(observer);
    }

    /**示例2、RxJava的基本接口采用链式的方式调用的使用验证
     *
     */
    //还是RxJavaDebug的功能类似，只不过换了一种链式，更加优雅而已
    public  void RxJavaDebugByChained() {
            // RxJava的流式操作
            Observable.create(new ObservableOnSubscribe<Integer>() {
                // 1. 创建被观察者 & 生产事件
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onComplete();
                }
            }).subscribe(new Observer<Integer>() {
                // 2. 通过通过订阅（subscribe）连接观察者和被观察者
                // 3. 创建观察者 & 定义响应事件的行为
                @Override
                public void onSubscribe(Disposable d) {
                    LogHelper.getInstance().w("开始采用subscribe连接");
                }
                // 默认最先调用复写的 onSubscribe（）

                @Override
                public void onNext(Integer value) {
                    LogHelper.getInstance().w("对Next事件"+ value +"作出响应"  );
                }

                @Override
                public void onError(Throwable e) {
                    LogHelper.getInstance().w("对Error事件作出响应");
                }

                @Override
                public void onComplete() {
                    LogHelper.getInstance().w("对Complete事件作出响应");
                }

            });
        }

    /**示例2、RxJava的基本接口采用链式的方式调用的使用验证
     *
     */
    //还是RxJavaDebug的链式功能,但是去除了onError/onComplete这两个不常用到的函数
    public  void RxJavaDebugByChainedSimple() {
//        // RxJava的流式操作
        Observable.just("hello","world")
                   .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                LogHelper.getInstance().w(s);
                            }
                        });

    }

    //map()操作
    //通过map()将原来的Observable对象类型转换成另一个Observable对象类型，
    // 同时将传输的数据进行一些灵活的操作，方便Observer获得想要的数据形式。
    public  void RxJavaDebugMap() {
//        // RxJava的流式操作
        Persion persion1=new Persion("CoCo",21);
        Persion persion2=new Persion("DoDo",22);
        Observable.just(persion1,persion2)
                   //使用map中的Function进行转换，参数1：转换前的类型，参数2：转换后的类型
                  .map(new Function<Persion, String>() {
                      @Override
                      public String apply(Persion persion) throws Exception {
                          return persion.getName();
                      }
                  })
                   .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogHelper.getInstance().w(s);
                    }
                });
    }

    //flatMap()操作
    //flatMap()对于数据的转换比map()更加彻底，
    //如果发送的数据是集合，flatmap()重新生成一个Observable对象，并把数据转换成Observer想要的数据形式。
    //它可以返回任何它想返回的Observable对象。
    public  void RxJavaDebugFlatMap() {
        List<Persion> persionList = new ArrayList<Persion>();
//        // RxJava的流式操作
        Persion persion1=new Persion("CoCo",21);
        Persion persion2=new Persion("DoDo",22);
        persionList.add(persion1);
        persionList.add(persion2);

        Observable.just(persionList)
                //使用map中的Function进行转换，参数1：转换前的类型，参数2：转换后的类型
                .flatMap(new Function<List<Persion>, ObservableSource<Persion>>() {
                    @Override
                    public ObservableSource<Persion> apply(List<Persion> persionList) throws Exception {
                        return Observable.fromIterable(persionList);
                    }
                })
                .subscribe(new Consumer<Persion>() {
                    @Override
                    public void accept(Persion persion) throws Exception {
                        LogHelper.getInstance().w(persion.getName());
                    }
                });
    }

    //flatMap()操作的前提下进行一些其他操作符的验证，例如；
    //    filter：集合进行过滤
    //    take：取指定数量的集合
    //    doOnNext()允许我们在每次输出一个元素之前做一些额外的事情
    public  void RxJavaDebugFlatMapUsedKeyword() {
        List<Persion> persionList = new ArrayList<Persion>();
//        // RxJava的流式操作
        Persion persion1=new Persion("CoCo",21);
        Persion persion2=new Persion("DoDo",22);
        Persion persion3=new Persion("EoEo",23);
        Persion persion4=new Persion("FoFo",24);
        persionList.add(persion1);
        persionList.add(persion2);
        persionList.add(persion3);
        persionList.add(persion4);
        Observable.just(persionList)
                //使用map中的Function进行转换，参数1：转换前的类型，参数2：转换后的类型
                .flatMap(new Function<List<Persion>, ObservableSource<Persion>>() {
                    @Override
                    public ObservableSource<Persion> apply(List<Persion> persionList) throws Exception {
                        return Observable.fromIterable(persionList);
                    }
                })
                //过滤
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object persion) throws Exception {
                        Persion persion_filter = (Persion) persion;
                        if (persion_filter.getName().equals("CoCo")) {
                            return false;
                        }
                        return true;
                    }
                })
                //取过滤后的前2个
                .take(2)
                //doOnNext()允许我们在每次输出一个元素之前做一些额外的事情。
                .doOnNext(new Consumer<Object>() {
                                      @Override
                                      public void accept(Object o) throws Exception {
                                          LogHelper.getInstance().w("可以做些准备工作,age="+((Persion)o).getAge());
                                      }})
                .subscribe(new Consumer<Persion>() {
                    @Override
                    public void accept(Persion persion) throws Exception {
                        LogHelper.getInstance().w(persion.getName());
                    }
                });
    }

    //使用Scheduler方法验证用于RxJavaDebug的Observable/observer在不同线程上的异步
    //subscribeOn(): 指定Observable(被观察者)所在的线程，或者叫做事件产生的线程。
    //observeOn():   指定 Observer(观察者)所运行在的线程，或者叫做事件消费的线程。
    public  void RxJavaDebugScheduler () {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                LogHelper.getInstance().w("所在的线程："+Thread.currentThread().getName());
                LogHelper.getInstance().w("发送的数据:"+1 + "");
                e.onNext(1);
            }
        })
        //subscribeOn(): 指定Observable(被观察者)所在的线程，或者叫做事件产生的线程。
        .subscribeOn(Schedulers.io())
        //observeOn():   指定 Observer(观察者)所运行在的线程，或者叫做事件消费的线程。
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogHelper.getInstance().w("所在的线程："+Thread.currentThread().getName());
                LogHelper.getInstance().w("接收到的数据:"+ "integer:" + integer);
            }
        });
    }


    //提示信息
    public void showHandlerMsg(String operation, Bundle bundle) {
        Message msg = new Message();
        msg.obj = operation;
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    //setter/getter
    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }


}
