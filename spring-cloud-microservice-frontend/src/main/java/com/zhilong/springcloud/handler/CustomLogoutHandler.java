package com.zhilong.springcloud.handler;

import com.zhilong.springcloud.contonst.PurchaseOnlieGlobalConstant;
import com.zhilong.springcloud.entity.TokenResponse;
import com.zhilong.springcloud.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Boolean isDel = redisUtils.del((Object)(PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + userDetails.getUsername()));
            logger.info("Remove cache in redis : " + isDel);
            logger.info(userDetails.getUsername() + " Log out.");
        } catch (Exception e) {
            logger.error("",e);
        }
    }
}
