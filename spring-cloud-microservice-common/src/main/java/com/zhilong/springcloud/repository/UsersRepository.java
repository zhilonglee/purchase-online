package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,String> {
    public Users findByUsername(@Param("username") String username);
}
