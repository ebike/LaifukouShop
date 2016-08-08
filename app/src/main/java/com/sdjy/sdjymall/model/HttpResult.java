package com.sdjy.sdjymall.model;

public class HttpResult<T> {
    //响应码：0代表失败，1代表成功
    public String code;
    //响应信息
    public String message;
    //返回的json数据
    public T data;

    public HttpResult() {
    }
}
