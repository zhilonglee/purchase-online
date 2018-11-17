package com.zhilong.springcloud.entity.bulider;

import com.zhilong.springcloud.bulider.EntityBuilderAdapter;
import com.zhilong.springcloud.entity.Person;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

public class PersonBuilder extends EntityBuilderAdapter<Person> {

    @Override
    public Person copy2EntityUsingNullValidation(Person source, Person target) {

        if (StringUtils.isNotBlank(source.getName())) {
            target.setName(source.getName());
        }
        if (source.getAge() != null) {
            target.setAge(source.getAge());
        }
        if (StringUtils.isNotBlank(source.getAddress())) {
            target.setAddress(source.getAddress());
        }
        if (source.getBirthDay() != null) {
            target.setBirthDay(source.getBirthDay());
        }
        if (source.getBirthDay() != null) {
            target.setBirthDay(source.getBirthDay());
        }
        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }
        if (source.getUsername() != null) {
            target.setUsername(source.getUsername());
        }
        if (source.getPassword() != null) {
            target.setPassword(source.getPassword());
        }

        return target;
    }

    @Override
    public Person copy2Entity(Person source, Person target, String... ignoreProperties) {
        BeanUtils.copyProperties(source,target,ignoreProperties);
        return target;
    }
}
