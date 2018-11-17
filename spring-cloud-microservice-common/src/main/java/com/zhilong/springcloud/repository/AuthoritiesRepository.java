package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.Authorities;
import com.zhilong.springcloud.entity.EmbeddedUsers;
import com.zhilong.springcloud.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities,EmbeddedUsers> {
}
