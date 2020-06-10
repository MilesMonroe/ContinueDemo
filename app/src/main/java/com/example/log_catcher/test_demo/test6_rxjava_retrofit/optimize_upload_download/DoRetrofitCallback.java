package com.example.log_catcher.test_demo.test6_rxjava_retrofit.optimize_upload_download;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** 扩展OkHttp的请求体，实现上传时的进度提示，用于优化Retrofit使用Part上传的流程
 *
 */
public abstract class DoRetrofitCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if(response.isSuccessful()) {
            onSuccess(call, response);
        } else {
            onFailure(call, new Throwable(response.message()));
        }
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public void onLoading(long total, long progress) {

    }
}
