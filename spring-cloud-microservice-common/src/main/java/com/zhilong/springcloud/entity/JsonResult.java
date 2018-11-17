package com.zhilong.springcloud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsonResult implements Serializable {

    Integer statusCode;
    String message;
    String info;

    public JsonResult(Integer statusCode, String message, String info) {
        this.statusCode = statusCode;
        this.message = message;
        this.info = info;
    }

    public static JsonResult ok(String message){
        return new JsonResult(200,message,"");
    }

    public static JsonResult badRequest(String message){
        return new JsonResult(400,message,"Bad Request!");
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
