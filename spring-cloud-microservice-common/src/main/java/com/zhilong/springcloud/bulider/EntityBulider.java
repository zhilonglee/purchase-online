package com.zhilong.springcloud.bulider;

public interface EntityBulider<T> {
    T copy2EntityUsingNullValidation(T source,T target);
    T copy2Entity(T source,T target,String... ignoreProperties);
}
