package com.example.log_catcher.test_demo.test6_rxjava_retrofit.another_download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public class TestDownloadListener {


    public interface DownloadListener {
        void onStart();//下载开始

        void onProgress(int progress);//下载进度

        void onFinish(String path);//下载完成

        void onFail(String errorInfo);//下载失败
    }

}
