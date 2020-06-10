package com.example.log_catcher.test_demo.test6_rxjava_retrofit.optimize_upload_download;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.example.log_catcher.test_demo.test6_rxjava_retrofit.Persion;
import com.example.log_catcher.test_demo.test6_rxjava_retrofit.RxJavaRetrofitTest;
import com.example.log_catcher.util.LogHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**用于验证优化后的文件上传接口
 *参考网址: https://blog.csdn.net/huyongl1989/article/details/52619236
 */
public class RetrofitUploadDownloadTest {

    Activity activity;
    Context mContext;
    public RetrofitUploadDownloadTest(Activity activity, Context mContext) {
        this.activity = activity;
        this.mContext = mContext;
    }

    //========================1、上传Demo演示，但没有可靠网址真正实验============================
    public interface FileUploadService {
        @Multipart
        @POST("fileService")
        Call<Persion> uploadFile(@Part MultipartBody.Part file);
    }


    /**这里部分的显示内容应在activity上实现，故这里注释,仅供参考;
     *
     */
    public static void doUpLoad(String url, File file){

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
//                .client(getOkHttpClient())//加入网络拦截器，用于搜集日志调试
                .build();

        //得到FileUploadService接口实例
        FileUploadService service = retrofit.create(FileUploadService.class);

        //创建符合Part上传格式的文件流body；
        RequestBody body1 = RequestBody.create(MediaType.parse("application/otcet-stream"), file);


        //这里继承了原Callback，并在类似覆写了原onResponse/onFailure接口
        DoRetrofitCallback<Persion> callback = new DoRetrofitCallback<Persion>() {
            @Override
            public void onSuccess(Call<Persion> call, Response<Persion> response) {

//                runOnUIThread(activity, response.body().toString());
                //进度更新结束
            }

            @Override
            public void onFailure(Call<Persion> call, Throwable t) {
                LogHelper.getInstance().e("请求异常！");
//                runOnUIThread(activity, t.getMessage());
                //进度更新结束
            }

            @Override
            public void onLoading(long total, long progress) {

                super.onLoading(total, progress);
                //此处进行进度更新
            }
        };

        //通过该行代码将RequestBody转换成特定的FileRequestBody
        FileRequestBody body = new FileRequestBody(body1, callback);

        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
        Call<Persion> call = service.uploadFile(part);


        call.enqueue(callback);

    }

    //========================2、普通下载方法Demo演示============================
    public interface FileDownloadService {
        //下载路径为潇烽的http://172.16.17.7:3300/Vi218-iAPP/libapp.zip.sgn
        @GET("libapp.zip.sgn")
        Call<ResponseBody> downloadFile();
    }
    /**这里部分的显示内容应在activity上实现，故这里注释,仅供参考;
     *
     */
    public static void doDownLoad(){

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(url) // 设置 网络请求 Url
                .baseUrl("http://172.16.17.7:3300/Vi218-iAPP/")
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .client(RxJavaRetrofitTest.getOkHttpClient())//加入网络拦截器，用于搜集日志调试
                .build();

        //得到FileUploadService接口实例
        FileDownloadService service = retrofit.create(FileDownloadService.class);

        //这里继承了原Callback，并在类似覆写了原onResponse/onFailure接口
        DoRetrofitCallback<ResponseBody> callback = new DoRetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    //从网络上读取文件字节
                    InputStream is = response.body().byteStream();
                    //本地存的文件路径
                    String path = Environment.getExternalStorageDirectory().toString()+"/Download"+"/download1.file";
                    File file = new File(path);
                    //创建文件
                    if (!file.exists()) {
                        if (!file.getParentFile().exists()) {
                            LogHelper.getInstance().w("file.getParentFile().mkdir()");
                            file.getParentFile().mkdir();
                        }
                        try {
                            LogHelper.getInstance().w("file.createNewFile()");
                            file.createNewFile();
                        } catch (IOException e) {
                            LogHelper.getInstance().e("file.createNewFile() ERR");
                            e.printStackTrace();
                        }
                    }
                    //建立流传输
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogHelper.getInstance().e("请求异常！");
//                runOnUIThread(activity, t.getMessage());
                //进度更新结束
            }

            @Override
            public void onLoading(long total, long progress) {

                super.onLoading(total, progress);
                //此处进行进度更新
            }
        };

        Call<ResponseBody> call = service.downloadFile();

        call.enqueue(callback);

    }

    private final String BASE_URL = "http://172.16.17.7:3300/Vi218-iAPP/";
    private <T> FileDownloadService getRetrofitService(final DoRetrofitCallback<T> callback) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                okhttp3.Response response = chain.proceed(chain.request());
                //将ResponseBody转换成我们需要的FileResponseBody
                return response.newBuilder().body(new FileResponseBody<T>(response.body(), callback)).build();
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileDownloadService service = retrofit.create(FileDownloadService.class);
        return service ;
    }
}
