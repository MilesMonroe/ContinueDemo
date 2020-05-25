package com.example.log_catcher.test_demo.okhttp_xml_jason_test;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.log_catcher.R;
import com.example.log_catcher.test_demo.xml_json_test.xml.XmlSaxHandlerHelper;
import com.example.log_catcher.util.LogHelper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okio.BufferedSink;


/**想要使用Okhttp需要前置以下条件:
 * 1、在builde.gradle里面添加上
 *      implementation 'com.squareup.okhttp3:okhttp:4.2.2'
 * 2、在AndroidManifest.xml添加所需权限
 *      <uses-permission android:name="android.permission.INTERNET" />
 *
 */
public class Okhttp4Demo extends LinearLayout implements View.OnClickListener {

    private Button bt_http_get,bt_http_post;
    OkHttpClient g_client;

    public Okhttp4Demo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout2_okhttp,this);

        initView();
        initData();
    }

    void initView(){

        bt_http_get = findViewById(R.id.bt_http_get);
        bt_http_get.setOnClickListener(this);

        bt_http_post = findViewById(R.id.bt_http_post);
        bt_http_post.setOnClickListener(this);
    }

    void initData(){
        //1、创建客户端实例
        g_client = new OkHttpClient();

        //1、1(选用)基于原client进一步配置已创建的客户端实例
//        OkHttpClient clientWith30sTimeout = g_client.newBuilder()
//        .readTimeout(30, TimeUnit.SECONDS)
//        .build();
//        Response response = clientWith30sTimeout.newCall(request).execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_http_get:
                okhttpGetDebug();
                break;
            case R.id.bt_http_post:
                okhttpPostDebug();
                break;
            default:
                break;
        }
    }


    public  void okhttpGetDebug(){

        OkhtttpGetTestRunnable okhtttpTestRunnable = new OkhtttpGetTestRunnable();
        try {
            Thread okhttpTestThread = new Thread(okhtttpTestRunnable);
            okhttpTestThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void okhttpPostDebug(){

        OkhtttpPostTestRunnable okhtttpTestRunnable = new OkhtttpPostTestRunnable();
        try {
            Thread okhttpTestThread = new Thread(okhtttpTestRunnable);
            okhttpTestThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class OkhtttpGetTestRunnable implements Runnable{

        @Override
        public void run() {
            LogHelper.getInstance().w("-----------okhttpDebug----------");

            //2、test GET
            try {
                //1、普通同步的Get请求测试
//                httpGet(g_client);

                //2、异步Get请求
//                httpGetASync(g_client);

                //4、超时cliet的实现
//                httpGetTimeout(g_client);

                //5、Cache cliet的实现
                httpGetWithCache(g_client);

                //9、请求授权证书测试
                //testAuthenticate(g_client);

            } catch (IOException e) {

                LogHelper.getInstance().e("----------捕捉到GET异常-----------");
                e.printStackTrace();
            }
        }
    }


    class OkhtttpPostTestRunnable implements Runnable{

        @Override
        public void run() {
            LogHelper.getInstance().e("--------------okhttpDebug-------------");

            //3、test POST
            try {
                //1、普通POST
//                httpPost(g_client);

                //2、POST a String
//                httpPostString(g_client);
                //3、POST File
//                httpPostFile(g_client);

                //4、POST FormBody
//                httpPostFormBody(g_client);

                //5、POST Stream
                //httpPostStream(g_client);

                //6、Post Multipart
                httpPostMultipart(g_client);

            } catch (IOException e) {
                LogHelper.getInstance().w("------捕捉到POST异常-------");
                e.printStackTrace();
            }
        }
    }


    //1.1、Http GET
    String httpGet(OkHttpClient client) throws IOException {
        String url = "https://www.baidu.com";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            //responseHeaders.size()=9
            //收到响应头如下：
            //Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform
            //Connection: keep-alive
            //Content-Type: text/html
            //Date: Wed, 20 May 2020 11:07:31 GMT
            //Last-Modified: Mon, 23 Jan 2017 13:23:55 GMT
            //Pragma: no-cache
            //Server: bfe/1.0.8.18
            //Set-Cookie: BDORZ=27315; max-age=86400; domain=.baidu.com; path=/
            //Transfer-Encoding: chunked
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }

    //1.2、Synchronous Get 流方式（同步Get）对于下载超过1MB的数据，采用流方式下载(未完待续)
    String httpGetSync(OkHttpClient client) throws IOException {
//        String url = "http://publicobject.com/helloworld.txt";
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        Response response = client.newCall(request).execute();
//        if (response.isSuccessful()) {
//
//            //打印Headers
//            LogHelper.getInstance().w("-------------打印Headers---------------");
//
//            Headers responseHeaders = response.headers();
//            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }
//            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
//            //数据太多，不打印
////            String responseBodyString = response.body().string();
//            InputStream responseBodyIs = response.body().byteStream();
////            LogHelper.getInstance().w(responseBodyIs);
//
//            responseBodyIs.reset();
//
//            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
//            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
//            LogHelper.getInstance().w(String.valueOf(response));
//
//            return responseBodyString;
//        } else {
//            LogHelper.getInstance().w("response="+response);
//            throw new IOException("Unexpected code:" + response.code());
//        }
        return "XXX";
    }

    //1.3、ASynchronous Get（异步Get）
    void httpGetASync(OkHttpClient client) throws IOException {
        //大数据（json）获取的网址
//        Request request = new Request.Builder()
//                .url("https://api.github.com/repos/square/okhttp/issues")
//                .header("User-Agent", "OkHttp Headers.java") //User-Agent的内容包含发出请求的用户信息
//                .addHeader("Accept", "application/json; q=0.5") //指定客户端能接收的类型
//                .addHeader("Accept", "application/vnd.github.v3+json")
//                .build();
//
        String url = "http://publicobject.com/helloworld.txt";
        Request request = new Request.Builder()
                .url(url)
                .build();
        LogHelper.getInstance().w("request="+request);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                LogHelper.getInstance().w("httpGetASync call Failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //打印Headers
                LogHelper.getInstance().w("-------------打印Headers---------------");
//                responseHeaders.size()=8
//                Server: nginx/1.10.0 (Ubuntu)
//                Date: Thu, 21 May 2020 07:40:02 GMT
//                Content-Type: text/plain
//                Content-Length: 1759
//                Last-Modified: Tue, 27 May 2014 02:35:47 GMT
//                Connection: keep-alive
//                ETag: "5383fa03-6df"
//                Accept-Ranges: bytes

                Headers responseHeaders = response.headers();
                LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
                for (int i = 0; i < responseHeaders.size(); i++) {
                    LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    //也可以使用Map方式获取:
//                    response.header("Date");
                }
                LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
                //数据太多，不打印
                String responseBodyString = response.body().string();
                LogHelper.getInstance().w(responseBodyString);

                LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
                LogHelper.getInstance().w("call = [" + call + "], response = [" + response + "]");
            }
        });

        return ;
    }


    //1.3、设置超时Client的Get
    void httpGetTimeout(OkHttpClient client) throws IOException {

        OkHttpClient timeoutClient =client.newBuilder().
                connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(6, TimeUnit.SECONDS)
                .readTimeout(9, TimeUnit.SECONDS)
                .build();

        String url = "http://httpbin.org/delay/20";//这个网址的最后字节即为设置的浏览延时
        Request request = new Request.Builder()
                .url(url)
                .build();

        LogHelper.getInstance().w("------->request="+request);
        Response response = timeoutClient.newCall(request).execute();
        if (response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");

            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }

        return ;
    }

    //1.4 CacheResponse
    // 为了缓存响应，你需要一个你可以读写的缓存目录，和缓存大小的限制。
    // 这个缓存目录应该是私有的，不信任的程序应不能读取缓存内容。
    // 一个缓存目录同时拥有多个缓存访问是错误的。大多数程序只需要调用一次new OkHttp()，在第一次调用时配置好缓存，然后其他地方只需要调用这个实例就可以了。否则两个缓存示例互相干扰，破坏响应缓存，而且有可能会导致程序崩溃。
    // 响应缓存使用HTTP头作为配置。你可以在请求头中添加Cache-Control: max-stale=3600 ,OkHttp缓存会支持。你的服务通过响应头确定响应缓存多长时间，例如使用Cache-Control: max-age=9600。
    void httpGetWithCache(OkHttpClient client) throws IOException {

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        //创建cache
        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory()+"Download"), cacheSize);

        //重新创建cacheClient
        OkHttpClient cacheClient =client.newBuilder()
                .cache(cache)
                .build();

        String url = "http://publicobject.com/helloworld.txt";

        Request request = new Request.Builder()
                .url(url)
                .build();

        LogHelper.getInstance().w("------->request="+request);

        String response1Body;
        Response response1 = cacheClient.newCall(request).execute();
            if (!response1.isSuccessful()) {
                throw new IOException("Unexpected code " + response1);
            }

            response1Body = response1.body().string();
            LogHelper.getInstance().w("Response 1 response:          " + response1);
            LogHelper.getInstance().w("Response 1 cache response:    " + response1.cacheResponse());
            LogHelper.getInstance().w("Response 1 network response:  " + response1.networkResponse());


        String response2Body;
        Response response2 = cacheClient.newCall(request).execute();
            if (!response2.isSuccessful()) {
                throw new IOException("Unexpected code " + response2);
            }

            response2Body = response2.body().string();
            LogHelper.getInstance().w("Response 2 response:          " + response2);
            LogHelper.getInstance().w("Response 2 cache response:    " + response2.cacheResponse());
            LogHelper.getInstance().w("Response 2 network response:  " + response2.networkResponse());


        LogHelper.getInstance().w("Response 2 equals Response 1? " + response1Body.equals(response2Body));

        //-------------------------得到的调试信息如下:-------------------------
        //<Thread-3>: Response 1 response:          Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
        //<Thread-3>: Response 1 cache response:    null
        //                    <Thread-3>: Response 1 network response:  Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
        //<Thread-3>: Response 2 response:          Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
        //<Thread-3>: Response 2 cache response:    null
        //                            <Thread-3>: Response 2 network response:  Response{protocol=http/1.1, code=200, message=OK, url=https://publicobject.com/helloworld.txt}
        //<Thread-3>: Response 2 equals Response 1? true


        return ;
    }


    //2.1、Http POST,提交空间只对,获取数据Json数据（作用和GET一样）
    //MediaType用于描述Http请求和响应体的内容类型，也就是Content-Type;
    //具体见:https://blog.csdn.net/xx326664162/article/details/77714126
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String httpPost(OkHttpClient client) throws IOException {

        String url = "https://www.csdn.net/";//这个可以成功

        // 0、构建一个RequestBody对象来存放待提交的参数，用于post
        RequestBody requestBody = new FormBody.Builder()
                .add("","")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }

    //2.2、Http POST,提交String
    //    使用HTTP POST提交请求到服务。这个例子提交了一个markdown文档到web服务，以HTML方式渲染markdown。
    //    因为整个请求体都在内存中，因此避免使用此api提交大文档（大于1MB）。
    String httpPostString(OkHttpClient client) throws IOException {

        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        String url = "https://api.github.com/markdown/raw";

        // 0、构建一个RequestBody对象来存放待提交的参数，用于post
//        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }
    //2.3、Http POST,上传文件
    String httpPostFile(OkHttpClient client) throws IOException {

        String url = "https://api.github.com/markdown/raw";

        // 0、构建一个RequestBody对象来存放待提交的参数，用于post
//        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");

        File file = new File(Environment.getExternalStorageDirectory().toString()+"/Download/"+"test_upload_file.md");

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }
    //2.4 Http POST FormBody（表单方式/这个示例暂时没有响应，进入可Exception)
    // 使用FormEncodingBuilder来构建和HTML标签相同效果的请求体。键值对将使用一种HTML兼容形式的URL编码来进行编码。
    String httpPostFormBody(OkHttpClient client) throws IOException {

        String url = "https://en.wikipedia.org/w/index.php";

        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }
    //2.5 Http POST Stream(这个示例暂时没有响应，进入可Exception)
    String httpPostStream(OkHttpClient client) throws IOException {

        String url = "https://api.github.com/markdown/raw";

        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");

        RequestBody requestBody = new RequestBody() {
            @Override public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }

    //2.6 Post方式提交分块请求
    // MultipartBuilder可以构建复杂的请求体，与HTML文件上传形式兼容。
    // 多块请求体中每块请求都是一个请求体，可以定义自己的请求头。
    // 这些请求头可以用来描述这块请求，例如他的Content-Disposition。
    // 如果Content-Length和Content-Type可用的话，他们会被自动添加到请求头中
    String httpPostMultipart(OkHttpClient client) throws IOException {

        String url = "https://api.imgur.com/3/image"; //该网址接连超时
        final String IMGUR_CLIENT_ID = "...";
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(url)
                .post(requestBody)
                .build();


        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }



    //9、请求授权证书
    String testAuthenticate(OkHttpClient client) throws IOException {

        // 授权证书
        OkHttpClient AuthClient =client.newBuilder().authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                LogHelper.getInstance().w("Authenticating for response: " + response);
                LogHelper.getInstance().w("Challenges: " + response.challenges());
                String credential = Credentials.basic("jesse", "password1");
                // HTTP授权的授权证书  Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
                return response
                        .request()
                        .newBuilder()
                        .header("Authorization", credential)
                        .build();
            }
        }).build();


        String url = "http://publicobject.com/secrets/hellosecret.txt";


        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = AuthClient.newCall(request).execute();
        if(response.isSuccessful()) {

            //打印Headers
            LogHelper.getInstance().w("-------------打印Headers---------------");
            Headers responseHeaders = response.headers();
            LogHelper.getInstance().w("responseHeaders.size()="+responseHeaders.size());
            for (int i = 0; i < responseHeaders.size(); i++) {
                LogHelper.getInstance().w(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            LogHelper.getInstance().w("\n\n-------------打印body.string---------------");
            //数据太多，不打印
            String responseBodyString = response.body().string();
            LogHelper.getInstance().w(responseBodyString);

            LogHelper.getInstance().w("\n\n-------------打印整个response---------------");
            //收到如下数据: Response{protocol=http/1.1, code=200, message=OK, url=https://www.baidu.com/}
            LogHelper.getInstance().w(String.valueOf(response));

            return responseBodyString;
        } else {
            LogHelper.getInstance().w("response="+response);
            throw new IOException("Unexpected code:" + response.code());
        }
    }

//    //10、PC端作为服务器，获取json数据（参考第一行代码做法）
//    void getPCJson(OkHttpClient client){
//        //2、创建一个http请求,注意Request.Builder()
//        Request request = new Request.Builder()
//                //                .url("https://www.baidu.com")
//
//                //用10.0.2.0去访问不到PC的apache服务器地址;
//                //所以:可以PC和安卓手机同连一个服务器，然后再PC端敲ifconfig确认无线网卡被分配到的网址（192.168.217.138）；然后在这个apk代码中敲入对应的访问地址（我是http://192.168.217.138:89/styles.xml），就能收到xml数据了；
//                //也可先在手机的浏览器端输入（http://192.168.217.138:89/styles.xml）确认是否能访问到;
//                .url("http://192.168.217.138:89/styles.xml")
//                //                .post(requestBody)
//                .build();
//        try {
//            //3、使用client的newCall()方法来创建一个Call对象，并调用它的execute()方法来发送请求并获取服务器的响应;
//            Response response;
//            response = client.newCall(request).execute();
//            //4、解析得到服务器返回的数据
//            //注意: response.body().string(其他方法类似)不能被重复调用，否者后面几次都会为null，因为获取后已在尾节点
//            //详解参考：https://blog.csdn.net/my_truelove/article/details/80133556
//            String responseData = response.body().string();
//            LogHelper.getInstance().w("\n\n--------------------------------------------");
//            LogHelper.getInstance().w("responseData="+responseData);
//            LogHelper.getInstance().w("--------------------------------------------\n\n");
//            if(response.isSuccessful()){
//                LogHelper.getInstance().w("response. isSuccessful");
//                //5、XML文件解析
//                //                XmlSaxHandlerHelper.parseXmlBySax(responseData);
//            }
//            else{
//                LogHelper.getInstance().w("response. Fail");
//            }
//
//        } catch (IOException e) {
//            LogHelper.getInstance().w("response error!!\n");
//            LogHelper.getInstance().w("--------------------------------------------\n\n");
//            e.printStackTrace();
//        }
//    }

}