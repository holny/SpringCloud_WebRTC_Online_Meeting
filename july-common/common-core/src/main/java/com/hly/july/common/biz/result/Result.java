package com.hly.july.common.biz.result;

import lombok.Data;


import java.io.Serializable;

/**
 * @author Linyuan Hou
 * @date 2021/3/24 19:05
 */
@Data
public class Result<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    private static <T> Result<T> result(int code, String msg, T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> failure(IResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> failure(IResultCode resultCode, String msg) {
        return result(resultCode.getCode(), msg, null);
    }

    public static <T> Result<T> failure(IResultCode resultCode, T data) {
        return result(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public static <T> Result<T> failure(IResultCode resultCode, String msg, T data) {
        return result(resultCode.getCode(), msg, data);
    }

    public static <T> Result<T> success(IResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> success(IResultCode resultCode,T data) {
        return result(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public static <T> Result<T> success(IResultCode resultCode, String msg) {
        return result(resultCode.getCode(), msg, null);
    }

    public static <T> Result<T> success(IResultCode resultCode, String msg, T data) {
        return result(resultCode.getCode(), msg, data);
    }

    public static <T> Result<T> success(T data) {
        return result(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success(String msg) {
        return result(ResultCode.SUCCESS.getCode(), msg, null);
    }

    public static <T> Result<T> success(String msg,T data) {
        return result(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> Result<T> success() {
        return result(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }
}
