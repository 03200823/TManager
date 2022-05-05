package com.students.app.http.model;

/**
 * time   : 2019/12/07
 * desc   : 统一接口数据结构
 */
public class HttpData<T> {

    private Boolean status;

    /** 返回码 */
    private int    code;
    /** 提示语 */
    private String msg;
    /** 数据 */
    private T      data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * 是否请求成功
     */
    public boolean isRequestSucceed() {
        return code == 1;
    }

    /**
     * 是否 Token 失效
     */
    public boolean isTokenFailure() {
        return code == 1001;
    }

}