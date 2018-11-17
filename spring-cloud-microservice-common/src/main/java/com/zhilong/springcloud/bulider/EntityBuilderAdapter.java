package com.zhilong.springcloud.bulider;

public abstract class EntityBuilderAdapter<T> implements EntityBulider<T> {

    @Override
    public T copy2EntityUsingNullValidation(T source, T target) { return null;}

    @Override
    public T copy2Entity(T source, T target, String... ignoreProperties) { return null;}
}
