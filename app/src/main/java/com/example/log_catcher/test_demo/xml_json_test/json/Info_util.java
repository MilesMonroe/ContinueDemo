package com.example.log_catcher.test_demo.xml_json_test.json;

public class Info_util {
    private int status;
    private String msg;
    private String data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "info_util [status=" + status + ", msg=" + msg + ", data="
                + data + "]";
    }

}
