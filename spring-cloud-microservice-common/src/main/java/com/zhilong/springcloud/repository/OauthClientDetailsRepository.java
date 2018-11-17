package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails,String> {
    public OauthClientDetails findByClientId(@Param("clientId") String clientId);
}
