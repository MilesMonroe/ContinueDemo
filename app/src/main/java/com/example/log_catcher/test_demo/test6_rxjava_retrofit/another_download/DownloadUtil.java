package com.example.log_catcher.test_demo.test6_rxjava_retrofit.another_download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**非拦截器方式下载文件;
* 参考网址:https://www.jianshu.com/p/4ad54d2f24c4
*
*/
public class DownloadUtil {

    public interface DownloadService {
        //默认情况下，Retrofit在处理结果前会将服务器端的Response全部读进内存。
        // 如果服务器端返回的是一个非常大的文件，则容易发生oom。
        // 使用@Streaming的主要作用就是把实时下载的字节就立马写入磁盘，而不用把整个文件读入内存
        @Streaming
        @GET
        Call<ResponseBody> download(@Url String url);
    }
    //http://172.16.17.7:3300/Vi218-iAPP/libapp.zip.sgn
    public static void download(String baseUrl, String url, final String path, final TestDownloadListener.DownloadListener downloadListener, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                //通过线程池获取一个线程，指定callback在子线程中运行。
                                //在Retrofit中，Callback默认运行在主线程中，如果我们直接将Response写到磁盘这一操作直接运行在主线程中，
                                // 会报NetworkOnMainThreadException异常。所以必须放在子线程中去运行。
                                .callbackExecutor(Executors.newSingleThreadExecutor())
                                .client(okHttpClient)//加入网络拦截器，用于搜集日志调试
                                .build();

        DownloadService service = retrofit.create(DownloadService.class);

        Call<ResponseBody> call = service.download(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                //将Response写入到从磁盘中，详见下面分析
                //注意，这个方法是运行在子线程中的
                writeResponseToDisk(path, response, downloadListener);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                downloadListener.onFail("网络错误～");
            }
        });
    }

    private static void writeResponseToDisk(String path, Response<ResponseBody> response, TestDownloadListener.DownloadListener downloadListener) {
        //从response获取输入流以及总大小
        writeFileFromIS(new File(path), response.body().byteStream(), response.body().contentLength(), downloadListener);
    }

    private static int sBufferSize = 8192;

    //将输入流写入文件
    private static void writeFileFromIS(File file, InputStream is, long totalLength, TestDownloadListener.DownloadListener downloadListener) {
        //开始下载
        downloadListener.onStart();

        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                downloadListener.onFail("createNewFile IOException");
            }
        }

        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
            }
            //下载完成，并返回保存的文件路径
            downloadListener.onFinish(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            downloadListener.onFail("IOException");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
