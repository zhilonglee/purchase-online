package com.zhilong.springcloud.repository;

import java.util.List;

import com.zhilong.springcloud.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>  {
	
    List<Person> findByAddress(String address);

    Person findByNameAndAddress(String name, String address);

    @Query("select p from Person p where p.name=:name and p.address=:address")
    Person withNameAndAddressQuery(@Param("name") String name, @Param("address") String address);
    
    @Modifying
    @Query("delete Person p where p.username = :username")
    int deleteByUsername(@Param("username") String username);

    @Modifying(clearAutomatically = true)
    @Query("update Person p set p.age = :age  where p.name = :name")
    int updateAgeByName(@Param("name") String name, @Param("age") Integer age);

    Person findByUsername(String username);

    Long countPersonByUsernameOrEmail(@Param(value = "username") String username,@Param(value = "email") String email);

/*    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Person p set p.address")
    int updatePersonBasicInfo();*/

}
