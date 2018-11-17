package com.zhilong.springcloud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsonListResult implements Serializable {
    Integer statusCode;
    List<Object> list = new ArrayList<>();
    String info;

}
