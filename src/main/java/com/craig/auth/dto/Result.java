package com.craig.auth.dto;

import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private boolean success;
    private String msg;

    public Result(T data) {
        this.data = data;
    }

    public Result(String msg){
        this.msg = msg;
        success = false;
    }

    public Result(){

    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> failed(String msg){
        Result<T> result = new Result<>(msg);
        return result;
    }
}
