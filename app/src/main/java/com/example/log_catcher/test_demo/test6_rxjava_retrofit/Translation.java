package com.example.log_catcher.test_demo.test6_rxjava_retrofit;

import com.example.log_catcher.util.LogHelper;

/**
 * 本类用于Retrofit 请求网址http://http//fanyi.youdao.com/openapi.do/?keyfrom=Yanzhikai&key=2032414398&type=data&doctype=json&version=1.1&q=car
 * 时的json解析
 *
 */
public class Translation {
    private int status;

    private content content;
    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private String ciba_use;
        private String ciba_out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void showJson() {
        LogHelper.getInstance().w("status="+status);
        LogHelper.getInstance().w("content.from="+content.from);
        LogHelper.getInstance().w("content.to="+content.to);
        LogHelper.getInstance().w("content.vendor="+content.vendor);
        LogHelper.getInstance().w("content.out="+content.out);
        LogHelper.getInstance().w("content.ciba_use="+content.ciba_use);
        LogHelper.getInstance().w("content.ciba_out="+content.ciba_out);
        LogHelper.getInstance().w("content.errNo="+content.errNo);
    }
}
