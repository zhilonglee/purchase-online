package com.zhilong.springcloud.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class Authorinfo {
    Integer age;
    String birthday;
    String[] habbit;

    @Override
    public String toString() {
        return "Authorinfo{" +
                "age=" + age +
                ", birthday='" + birthday + '\'' +
                ", habbit=" + Arrays.toString(habbit) +
                '}';
    }
}
