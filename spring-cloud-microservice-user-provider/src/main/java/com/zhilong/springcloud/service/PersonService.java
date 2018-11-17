package com.zhilong.springcloud.service;

import com.zhilong.springcloud.entity.JsonResult;
import com.zhilong.springcloud.entity.Person;
import com.zhilong.springcloud.entity.enu.TokenMoudle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PersonService extends  BaseService<Person>{

    Page<Person> findAll(Person t, Pageable pageable)  throws Exception ;

    List<Person> findByAddress(String address);

    Person findByUsername(String username);

    Person findByNameAndAddress(String name, String address);

    Person withNameAndAddressQuery(String name, String address);

    int deleteByUsername(String username);

    int updateAgeByName(String name, Integer age);

    void update(Person person)  throws Exception;

    Long countPersonByUsernameOrEmail(Person person);

    ResponseEntity updatePersonAllfileds(String username, Person personparam);

    ResponseEntity register(Person person);

    ResponseEntity updatePersonBasicInfo(Long id,Person personParam);

    String createToken(String username, TokenMoudle tokenMoudle);

    ResponseEntity<JsonResult> active(String token, TokenMoudle tokenMoudle);

    void resetPasswordByToken(String password, String token);
}
