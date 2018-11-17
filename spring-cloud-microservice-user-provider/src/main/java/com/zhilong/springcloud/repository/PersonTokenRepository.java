package com.zhilong.springcloud.repository;

import com.zhilong.springcloud.entity.PersonToken;
import com.zhilong.springcloud.entity.enu.TokenMoudle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

public interface PersonTokenRepository extends JpaRepository<PersonToken, Long>, JpaSpecificationExecutor<PersonToken> {
    PersonToken findPersonTokenByTokenAndTokenMoudle(@Param("token") String token, @Param("tokenMoudle")TokenMoudle tokenMoudle);
}
