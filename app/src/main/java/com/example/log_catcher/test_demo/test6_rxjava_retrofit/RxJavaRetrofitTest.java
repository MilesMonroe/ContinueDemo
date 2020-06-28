package com.example.log_catcher.test_demo.test6_rxjava_retrofit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.log_catcher.R;
import com.example.log_catcher.test_demo.test6_rxjava_retrofit.another_download.DownloadUtil;
import com.example.log_catcher.test_demo.test6_rxjava_retrofit.another_download.TestDownloadListener;
import com.example.log_catcher.test_demo.test6_rxjava_retrofit.optimize_upload_download.RetrofitUploadDownloadTest;
import com.example.log_catcher.util.FileUtils;
import com.example.log_catcher.util.LogHelper;

import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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
public class RxJavaRetrofitTest extends LinearLayout implements View.OnClickListener, TestDownloadListener.DownloadListener {

    private Button bt_rxjava_test1, bt_rxjava_test2, bt_rxjava_test3, bt_rxjava_test4,
            bt_rxjava_test5, bt_rxjava_test6, bt_rxjava_test7, bt_rxjava_test8,
            bt_retrofit_test1, bt_retrofit_test2, bt_retrofit_test3, bt_retrofit_test4,
            bt_retrofit_test5, bt_retrofit_test6;
    private Handler mHandler;
    private Context mContext;
    private ImageView image1;


    public RxJavaRetrofitTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.demo6_rxjava_retrofit, this);
        mContext = context;
        initView();
        initData();
        LogHelper.getInstance().e("XmlJasonParseTest In,attrs=" + attrs);
    }

    void initView() {

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

        bt_retrofit_test3 = findViewById(R.id.bt_retrofit_test3);
        bt_retrofit_test3.setOnClickListener(this);

        bt_retrofit_test4 = findViewById(R.id.bt_retrofit_test4);
        bt_retrofit_test4.setOnClickListener(this);

        bt_retrofit_test5 = findViewById(R.id.bt_retrofit_test5);
        bt_retrofit_test5.setOnClickListener(this);

        bt_retrofit_test6 = findViewById(R.id.bt_retrofit_test6);
        bt_retrofit_test6.setOnClickListener(this);

        image1 = findViewById(R.id.image1);
    }

    void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //===============rxjava测试===========
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
                differMapAndFlatMap();
                break;

            //===============Retrofit测试===========
            case R.id.bt_retrofit_test1:
                LogHelper.getInstance().w("-------------bt_retrofit_test1------------");
                retrofitGet();
                break;
            case R.id.bt_retrofit_test2:
                LogHelper.getInstance().w("-------------bt_retrofit_test2------------");
                retrofitGetWithRxJava();
                break;
            case R.id.bt_retrofit_test3:
                LogHelper.getInstance().w("-------------bt_retrofit_test3------------");
                retrofitGetByGson();
                break;
            case R.id.bt_retrofit_test4:
                LogHelper.getInstance().w("-------------bt_retrofit_test4------------");
                retrofitPostByGson();
                break;
            case R.id.bt_retrofit_test5:
                LogHelper.getInstance().w("-------------bt_retrofit_test5------------");
                //常规的上传操作;
                retrofitPartByGson();

                //优化后的Part文件上传，模板,仅供参考
//                RetrofitUploadDownloadTest.doUpLoad("www.test.com",new File(Environment.getExternalStorageState().toString()+"/Download/test.txt"));
                break;
            case R.id.bt_retrofit_test6:
                LogHelper.getInstance().w("-------------bt_retrofit_test6------------");
                //1、普通下载方法Demo演示，开启潇峰的服务器，可成功
//                RetrofitUploadDownloadTest.doDownLoad();

                //2、加了进度条的下载方法Demo演示，开启潇峰的服务器，可成功
                retrofitDwnload();
                break;
            default:
                break;
        }
    }

    //========================================Rxjava部分==================================

    /**
     * 示例1、RxJava的基本接口使用验证
     */
    public void RxJavaDebug() {
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
        Observable observable1 = Observable.create(new ObservableOnSubscribe<String>() {
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

    /**
     * 示例2、RxJava的基本接口采用链式的方式调用的使用验证
     */
    //还是RxJavaDebug的功能类似，只不过换了一种链式，更加优雅而已
    public void RxJavaDebugByChained() {
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
                LogHelper.getInstance().w("对Next事件" + value + "作出响应");
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

    /**
     * 示例2、RxJava的基本接口采用链式的方式调用的使用验证
     */
    //还是RxJavaDebug的链式功能,但是去除了onError/onComplete这两个不常用到的函数
    public void RxJavaDebugByChainedSimple() {
//        // RxJava的流式操作
        Observable.just("hello", "world")
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
    public void RxJavaDebugMap() {
//        // RxJava的流式操作
        Persion persion1 = new Persion("CoCo", 21);
        Persion persion2 = new Persion("DoDo", 22);
        Observable.just(persion1, persion2)
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


    //flatMap()操作方式1
    //flatMap()对于数据的转换比map()更加彻底，
    //如果发送的数据是集合，flatmap()重新生成一个Observable对象，并把数据转换成Observer想要的数据形式。
    //它可以返回任何它想返回的Observable对象。
//    public  void RxJavaDebugFlatMap() {
//        List<Persion> persionList = new ArrayList<Persion>();
////        // RxJava的流式操作
//        Persion persion1=new Persion("CoCo",21);
//        Persion persion2=new Persion("DoDo",22);
//        persionList.add(persion1);
//        persionList.add(persion2);
//
//        Observable.just(persionList)
//                //使用map中的Function进行转换，参数1：转换前的类型，参数2：转换后的类型
//                .flatMap(new Function<List<Persion>, ObservableSource<Persion>>() {
//                    @Override
//                    public ObservableSource<Persion> apply(List<Persion> persionList) throws Exception {
//                        return Observable.fromIterable(persionList);
//                    }
//                })
//                .subscribe(new Consumer<Persion>() {
//                    @Override
//                    public void accept(Persion persion) throws Exception {
//                        LogHelper.getInstance().w(persion.getName());
//                    }
//                });
//    }
    //flatMap()操作方式2
    public void RxJavaDebugFlatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                String flatMap = "I am value " + integer;
                return Observable.just(flatMap);
            }
        }).subscribe(new Consumer<String>() {

            @Override
            public void accept(String s) throws Exception {
                LogHelper.getInstance().w("flatMap : accept : " + s + "\n");
            }
        });
    }
    /**
         map和flatMap都是依赖传入的Function对数据进行变换:
         但是区别在于
         1、返回值上面：
             map变换后可以返回任意值，而flatMap则只能返回ObservableSource类型
         2、变换后的输出：
             map只能进行一对一的变换，而flatMap则可以进行一对一，一对多，多对多的变换，具体的变换规则根据我们设置的变换函数mapper来定
    */
    void differMapAndFlatMap(){
        //准备数据
        List<String> list = new ArrayList<>();
            list.add("b");
            list.add("a");
            list.add("c");

            //map:一对一
            LogHelper.getInstance().w("==map 一对一:[b, a, c]-->[b, a, c]===");
            Observable.just(list)
                    .map(new Function<List<String>, List<String>>() {
                        @Override
                        public List<String> apply (List<String> strings) throws Exception {
                            return strings;
                        }
                    })
                    .subscribe(s ->{
                        LogHelper.getInstance().w(s.toString());
                    });
            //flatMap:1对1
            LogHelper.getInstance().w("===1对1:[b, a, c]-->[b, a, c]===");
            Observable.just(list)
                    .flatMap(new Function<List<String>, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply (List < String > s) throws Exception {
                //                                LogHelper.getInstance().w("map--1----" + s);
                            return Observable.fromArray(s);
                        }
                            })
                    .subscribe(s ->{
                        LogHelper.getInstance().w(s.toString());
            });

            //flatMap:1对多
            LogHelper.getInstance().w("===1对多:[b, a, c]-->b, a, c===");
            Observable.just(list)
                      .flatMap(new Function<List<String>, ObservableSource<?>>() {
                         @Override
                         public ObservableSource<?> apply (List < String > s) throws Exception {
    //                                             LogHelper.getInstance().w("map--1----" + s);
                             return Observable.fromIterable(s);
                         }})
                       .subscribe(s -> {
                            LogHelper.getInstance().w(s.toString());
                        });

            //flatMap:多对多
            LogHelper.getInstance().w("===多对多:a, b, c-->[a, c]===");
            Observable.just("a","b","c")
                    .flatMap(new Function<String, ObservableSource<?>>() {
                         @Override
                         public ObservableSource<?> apply (String s) throws Exception {
    //                                             LogHelper.getInstance().w("map--1----" + s);
                             if (s.equalsIgnoreCase("b")) return Observable.empty();
                             return Observable.just(s);
                         }})
                      .subscribe(s ->{
                            LogHelper.getInstance().w(s.toString());
                        });

           //flatMap:1对多
            LogHelper.getInstance().w("===多对多:a, b, c-->a,b,c,d===");
            Observable.just("a","b","c")
                      .flatMap(new Function<String, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply (String s) throws Exception {
                    //                                LogHelper.getInstance().w("map--1----" + s);
                                if (s.equalsIgnoreCase("c")) return Observable.just("c", "d");
                                return Observable.just(s);
                            }
                        })
                      .subscribe(s ->{
                                LogHelper.getInstance().w(s.toString());
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

//========================================Retrofit部分==================================
    //==========示例1、最简单的Retrofit请求使用=========
    /**步骤1、创建用于配置网络请求的的接口
     * 何为配置网络请求参数？
     * 说白了就是设置网络请求的地址是什么、使用什么请求方法、传递的参数是什么、返回值类型是什么等等
     * 一系列配置信息，通过接口描述清楚这些具体的网络配置信息，Retrofit才能根据你的配置发起最终的网络请求
     */
    public interface GetRequestInterface {
        /**
         *
         *
         * 通过get（）方法获取图片的请求接口
         * GET注解中的参数值"2019-06-29-121904.png"和Retrofit的base url拼接在一起,
         * 就是本次请求的最终地址
         *
         * @return
         *
         */
        @GET("2019-06-29-121904.png")
        Call<ResponseBody> getPictureCall();
    }

    /**步骤2、创建Retrofit实例并发起网络请求，点按钮将下载下来的图片显示在ImageView中:
     * 实例来源: https://blog.csdn.net/qq_36982160/article/details/94201257
     *备注:<细心的读者可能发现了一个问题，
     * 在上面的代码中我是直接在CallBack接口的onResponse()回调方法中更新的ImageView，
     * 即在onResponse()中更新UI的，在OkHttp中这个onResponse()方法是在子线程中被调用的，
     * 而在Retrofit中，onResponse()和onFailure（）这两个回调方法都是在主线程进行调用的，
     * 所以可以在其中直接更新UI，这也是Retrofit和OkHttp的一点不同之处>
     */
    public void retrofitGet() {
        /*
         创建Retrofit对象，这里设置了baseUrl，注意我们在声明网络配置接口GetRequestInterface的时候在GET注解中也声明了一个Url，
         我们将会这里的baseUrl和GET注解中设置的Url拼接之后就可以形成最终网络请求实际访问的url
         */
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://picture-pool.oss-cn-beijing.aliyuncs.com/")
                                .build();

        //通过反射，创建网络请求配置的接口实例
        GetRequestInterface getRequestInterface = retrofit.create(GetRequestInterface.class);

        //调用我们声明的getPictureCall（）方法创建Call对象
        Call<ResponseBody> requestBodyCall = getRequestInterface.getPictureCall();

        //使用requestBodyCall发起异步网络请求
        requestBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //将网络请求返回的数据解析为图片并展示到界面
                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                Drawable drawable = Drawable.createFromStream(inputStream, "pic.png");
                image1.setBackground(drawable);
                LogHelper.getInstance().w( "网络请求成功");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogHelper.getInstance().w( "网络请求失败，失败原因：" + t.getMessage());
            }
        });


    }

    //=====示例2、结合Rxjava后的简单Retrofit请求使用=====
    public interface GetRequestInterfaceWithRxJava {
        /**
         * 通过get（）方法获取图片的请求接口
         * GET注解中的参数值"2019-06-29-121904.png"和Retrofit的base url拼接在一起就是本次请求的最终地址
         *
         设置返回值类型为Observable的
         * @return
         */
        @GET("2019-06-29-121904.png")
        Observable<ResponseBody> getPictureCall();
    }

    public void retrofitGetWithRxJava() {
        /*
         创建Retrofit对象，这里设置了baseUrl，注意我们在声明网络配置接口GetRequestInterface的时候在GET注解中也声明了一个Url，
         我们将会这里的baseUrl和GET注解中设置的Url拼接之后就可以形成最终网络请求实际访问的url
         */
        Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://picture-pool.oss-cn-beijing.aliyuncs.com/")
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build();

        //创建网络请求配置的接口实例
        GetRequestInterfaceWithRxJava getRequestInterfaceWithRxJava = retrofit.create(GetRequestInterfaceWithRxJava.class);

        //调用我们声明的getPictureCall（）方法创建Call对象
        Observable<ResponseBody> requestObservable = getRequestInterfaceWithRxJava.getPictureCall();

                         //subscribeOn(): 指定Observable(被观察者)所在的线程，或者叫做事件产生的线程。
        requestObservable.subscribeOn(Schedulers.io())
                          //observeOn():   指定 Observer(观察者)所运行在的线程，或者叫做事件消费的线程。
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribe(new Observer<ResponseBody>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    LogHelper.getInstance().w("onSubscribe");
                                }

                                @Override
                                public void onNext(ResponseBody body) {
                                    //将网络请求返回的数据解析为图片并展示到界面
                                    InputStream inputStream = body.byteStream();
                                    Drawable drawable = Drawable.createFromStream(inputStream, "pic.png");
                                    image1.setBackground(drawable);
                                    LogHelper.getInstance().w("网络请求成功");

                                }

                                @Override
                                public void onError(Throwable e) {
                                    LogHelper.getInstance().w("onError:" + e.getMessage());
                                }

                                @Override
                                public void onComplete() {
                                    LogHelper.getInstance().w("onComplete");
                                }
                          });


    }

    //============示例3、结合GET之后通过Gson解析报文===========
    //示例1、普通GET；实例参考网址:https://blog.csdn.net/carson_ho/article/details/73732076
    public interface GetRequest_Interface {

        @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
        Call<Translation> getCall();
    }

    //示例2、 Path关键字使用；
    // 作用：在发起请求时， {queryContent} 会被替换为方法的第一个参数 user（被@Path注解作用
    public interface GetRequestWithPath_Interface {
//        @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
        @GET("{ajax}.php?a=fy&f=auto&t=auto&w=eat%20apple")
        //注意？后面的内容不能用@Path关键字，因为是动态的，需要用Query；否则会报错;
        //例如: @GET("ajax.php?a=fy&f=auto&t=auto&w={translateContent}"),这样是不行的！！！
        Call<Translation> getCall(@Path("ajax") String repalceContent);
    }

    //示例3、 Query关键字使用；@Query和@QueryMap
    // 作用：用于 @GET 方法的查询参数（Query = Url 中 ‘?’ 后面的 key-value）
    // 《例如: url = http://www.println.net/?cate=android，其中，Query = cate
    //        @GET("/")
    //        Call<String> cate(@Query("cate") String cate);
    //  》
    public interface GetRequestWithQuery_Interface {
//        @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
        @GET("ajax.php")
        Call<Translation> getCall(@QueryMap HashMap<String, String>queryContent);
    }


    /**本示例完成实现了retrofit普通Get，通过Gson解析报文，
     * 并完成@Path、@Query、@QueryMap关键字测试;
     */
    public void retrofitGetByGson() {
        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        //==============1、普通GET===========
//        // 步骤5:创建 网络请求接口 的实例
//        GetRequest_Interface requestCall = retrofit.create(GetRequest_InterfaceField.class);
//        //对 发送请求 进行封装
//        Call<Translation> call = requestCall.getCall();

        //==============2、GET中@Path关键字替换===========
//        // 步骤5:创建 网络请求接口 的实例
//        GetRequestWithPath_Interface requestCall = retrofit.create(GetRequestWithPath_Interface.class);
//        //对 发送请求 进行封装
//        Call<Translation> call = requestCall.getCall("ajax");

        //==============3、GET中@Path关键字替换===========
        // 步骤5:创建 网络请求接口 的实例
        GetRequestWithQuery_Interface requestCall = retrofit.create(GetRequestWithQuery_Interface.class);
//      @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
        HashMap<String, String> queryContent = new HashMap<>();
        queryContent.put("a", "fy");
        queryContent.put("f", "auto");
        queryContent.put("t", "auto");
        queryContent.put("w", "hello bird");

        //对 发送请求 进行封装
        Call<Translation> call = requestCall.getCall(queryContent);


        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 步骤7：处理返回的数据结果
                response.body().showJson();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                LogHelper.getInstance().w("连接失败");
            }
        });
    }


    //=====================4.1 POST方法与Field/FieldMap关键字使用====================
    // 示例4.1、 POST方法与Field/FieldMap关键字使用；
    // 作用：Field/FieldMap用在发送Post请求时body中向服务器提交请求的表单字段
    // 注意: 1、Field/FieldMap需要与关键字@FormUrlEncoded配合使用;
    //      采用@FormUrlEncoded注解的原因:API规定采用请求格式x-www-form-urlencoded,即表单形式
    //      2、Field/FieldMap与Query/QueryMap区别体现在:
//             Query/QueryMap的数据体现在url上使用
//             Field/FieldMap的数据体现在请求体上；(两者的最终效果一致;)
    public interface PostRequestFiled_Interface {

        /**
         * Field关键字
         */
        @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
        //必须与 @FormUrlEncoded 注解配合使用
        @FormUrlEncoded
        //类似
        Call<TranslationYouDao> getCall(@Field("i") String targetSentence);


//        /**
//         * FieldMap的key作为表单的键
//         */
//        @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
//        @FormUrlEncoded
//        Call<ResponseBody> testFormUrlEncoded2(@FieldMap Map<String, Object> map);


    }

    //=====================4.2 POST方法与@Body关键字的使用====================
    // 示例4.2、 POST方法与@Body关键字的使用
    // 作用：以 Post方式 传递 自定义数据类型 给服务器
    // 特别注意：如果提交的是一个Map，那么作用相当于 @Field
    //   《Map要经过 FormBody.Builder 类处理成为符合 Okhttp 格式的表单，如：
    //    RequestBody formBody = new FormBody.Builder()
    //            .add("i", "I love you")
    //            .build();
    public interface PostRequestBody_Interface {

        @POST("translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")

            //类似
        Call<TranslationYouDao> getCall(@Body RequestBody requestBody);
    }

    /**本示例完成实现了retrofit普通POST，通过Gson解析报文，
     * 并完成@Filed\@Body关键字测试;
     */
    public void retrofitPostByGson() {
        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .client(getOkHttpClient())//加入网络拦截器，用于搜集日志调试
                .build();

        //==============4.1、普通POST+Filed===========
//        PostRequestFiled_Interface request = retrofit.create(PostRequestFiled_Interface.class);
//
//        //匹配4.1实例 Field使用对 发送请求 进行封装(设置需要翻译的内容)
//        Call<TranslationYouDao> call = request.getCall("I love you");

        //匹配4.1实例 FieldMap的话与QueryMap类似，以下只为实例:
//        // @FieldMap
//        // 实现的效果与上面相同，但要传入Map
//        Map<String, Object> map = new HashMap<>();
//        map.put("username", "Carson");
//        map.put("age", 24);
//        Call<ResponseBody> call2 = service.testFormUrlEncoded2(map);

        //==============4.2、普通POST+Body类型的表单提交===========
        PostRequestBody_Interface request = retrofit.create(PostRequestBody_Interface.class);

        // 匹配4.2实例@body与FormBody.Builder使用对 发送请求 进行封装(设置需要翻译的内容)
        RequestBody formBody = new FormBody.Builder()
                .add("i", "I love you")//这个i是格式要求（body中查询的头）
                .build();

         Call<TranslationYouDao> call = request.getCall(formBody);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<TranslationYouDao>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<TranslationYouDao> call, Response<TranslationYouDao> response) {
                // 步骤7：处理返回的数据结果：输出翻译的内容
                LogHelper.getInstance().w(response.body().getTranslateResult().get(0).get(0).getTgt());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<TranslationYouDao> call, Throwable throwable) {
                LogHelper.getInstance().w("连接失败");
                LogHelper.getInstance().w(throwable.getMessage());
            }
        });
    }


    // =====================示例4.3、 @Multipart配合@Part方法上传文件======================
    // 作用：其中@Multipart：表示发送form-encoded的数据（适用于 有文件 上传的场景）（类似@Field关键字中配合的@FormUrlEncoded ）
    //      @Part表示每个键值对需要用@Part来注解键名，随后的对象需要提供值;
    // 注意: Part 和 Field 究竟有什么区别，其实从功能上讲，
    //     无非就是客户端向服务端发起请求携带参数的方式不同，
    //     并且前者可以携带的参数类型更加丰富，包括数据流；
    //    参考网址:https://www.cnblogs.com/zhujiabin/p/7601658.html
    public interface FileUploadService {
        @Multipart
        @POST("upload")
        Call<ResponseBody> upload(@Part("description") RequestBody description,
                                  @Part MultipartBody.Part file);
    }

    /**本示例完成实现了retrofit上传文件的演示
     * 并完成@Part关键字测试;
     */
    public void retrofitPartByGson() {
        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.println.net/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .client(getOkHttpClient())//加入网络拦截器，用于搜集日志调试
                .build();

        //==============4.2、普通POST+Part类型的表单提交===========
        FileUploadService service = retrofit.create(FileUploadService.class);

        //构建要上传的文件
        String FilePath = Environment.getExternalStorageDirectory().toString() + "/Download/ET128.txt";
        File file = new File(FilePath);

        //告诉服务器所传媒体的类型
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);

        //============ MultipartBody.Part.createFormData============
        //入参1、这个参数名称是服务端 request.getParmars()要用的 是和服端约定好的 不要定错。
        //入参2、文件名也就是你本地上传文件的文件名 这个随便写无所谓。
        //入参3、body 我们可以用RequestBody.create 构建就可以了。
        //具体参考:https://www.jianshu.com/p/d109c7192586
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);


        String descriptionString = "This is a description";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        //上传文件
        Call<ResponseBody> call = service.upload(description, body);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>()  {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 步骤7：处理返回的数据结果：输出翻译的内容
                LogHelper.getInstance().w(response.body().toString());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                LogHelper.getInstance().w("连接失败");
                LogHelper.getInstance().w(throwable.getMessage());
            }
        });
    }




    /**本示例实现下载例程，
     * 并完成@Part关键字测试;
     */
    public void retrofitDwnload() {
        String basrUrl = "http://172.16.17.7:3300/Vi218-iAPP/";
        String url = "libapp.zip.sgn";
        String savePath = Environment.getExternalStorageDirectory().toString()+"/Download"+"/download.file";

        DownloadUtil.download(basrUrl,url, savePath, RxJavaRetrofitTest.this,getOkHttpClient());

    }
    @Override
    public void onStart() {
        //运行在子线程
        LogHelper.getInstance().w("=======>download start");
    }

    @Override
    public void onProgress(int progress) {
        //运行在子线程
        LogHelper.getInstance().w("=======>download progress="+progress+"%");
    }

    @Override
    public void onFinish(String path) {
        //运行在子线程
        LogHelper.getInstance().w("=======>download onFinish, save path="+path);
    }

    @Override
    public void onFail(String errorInfo) {
        //运行在子线程
        LogHelper.getInstance().w("=======>download onFail");
    }

    /**初始化网络拦截器，用于搜集日志调试
     * 实现步骤参考网址:https://blog.csdn.net/weixin_43115440/article/details/83306515
     * @return
     */
    public static OkHttpClient getOkHttpClient() {

        //日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogHelper.getInstance().w("OkHttp====Message:"+message);
            }
        });
        loggingInterceptor.setLevel(level);
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder();
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }



    //=====================主线程中消息弹框====================
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

    //===========HashMap转化为json实例==================
//    Gson gson = new Gson();
//    HashMap<String, String> map = new HashMap<>();
//map.put("nickname", "123456");
//map.put("password, "abcdefg");
//    String requestBody = gson.toJson(map);


}
