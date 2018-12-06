package com.zhilong.springcloud.frontend;


import com.zhilong.springcloud.contonst.PurchaseOnlieGlobalConstant;
import com.zhilong.springcloud.entity.JsonListResult;
import com.zhilong.springcloud.entity.JsonResult;
import com.zhilong.springcloud.listener.SessionContext;
import com.zhilong.springcloud.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class CommonController {

    private final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisUtils redisUtils;

    @RequestMapping("/footer")
    public String footer() {
        return "views/footer";
    }

    @RequestMapping(value = {"/index","/"})
    public String index(Model model, HttpServletRequest request) {

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if ( principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            getCurrentSessionAndAddinSessionContext(request, userDetails);
            model.addAttribute("userDetails",userDetails);
        }

        // there is remained a bug: First time,Access the system via login function.If authentication is passed,
        // the website will redirect to /index page. But owing to request redirection, the session can not be created right now.
        // When users access /index or / page again, the session can be got.



        return "content/index";
    }

    @GetMapping("/login")
    public String login() {
        return "content/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "content/register";
    }

    @RequestMapping("/active")
    public String active() {
        return "content/active";
    }

    @RequestMapping("/resetpassword")
    public String resetpassword() {
        return "content/resetpassword";
    }

    @RequestMapping("/newpassword")
    public String newpassword() {
        return "content/newpassword";
    }

    @RequestMapping("/payment")
    public String payment() {
        return "content/payment";
    }

    @RequestMapping("/mycart")
    public String cart() {
        return "content/mycart";
    }


    @Scheduled(cron = "0 0/1 * * * *")
    @ResponseBody
    @PostMapping("/check/oauth2/token")
    public String checkOauth2TokenIsExpired(){
        logger.info("checkOauth2TokenIsExpired -- method access");
        UserTokenExtendViaSessions();
        return "completed";
    }


    @RequestMapping("/redis/keys")
    public HttpEntity redisKeys(){
        Set<String> keys = redisUtils.keys(("*" + PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + "*"));
        return ResponseEntity.ok().body(keys);
    }


    @RequestMapping("/session/list")
    public HttpEntity showSessionId(){
        SessionContext sessionContext = SessionContext.getInstance();
        return ResponseEntity.ok().body(sessionContext.getSessionIdSet());
    }

    private void getCurrentSessionAndAddinSessionContext(HttpServletRequest request, UserDetails userDetails) {
        HttpSession session = request.getSession(false);
        logger.info("session Id : " + (session != null ? session.getId() : "no session"));
        if (session != null) {
            String username = userDetails.getUsername();
            session.setAttribute("username", username);
        }

        SessionContext sessionContext = SessionContext.getInstance();
        sessionContext.addSession(session);
    }

    private void UserTokenExtendViaSessions() {
        try {
            // Set<String> keys = redisUtils.keys(("*" + PurchaseOnlieGlobalConstant.OAUTH2_TOKEN_IN_REDIS_PREFIX + "*"));
            SessionContext sessionContext = SessionContext.getInstance();
            Set<String> sessionIdSet = sessionContext.getSessionIdSet();
            sessionIdSet.forEach((String sessionId) -> {
                logger.info("session Id : " +  sessionId);
                HttpSession session = sessionContext.getSession(sessionId);
                String responseEntity = "";
                if (session != null) {
                    String username = (String)session.getAttribute("username");
                    if (username != null && username != "") {
                        responseEntity = restTemplate.postForObject(
                                "http://192.168.137.10:8042/user/v1/person/oauth/token/extend/" +
                                        username,
                                null,
                                String.class);
                        if (responseEntity.contains(HttpStatus.BAD_REQUEST.value()+"")) {
                            session.invalidate();
                        }
                    }
                }

            });
        } catch (Exception e) {
            logger.error("",e);
        }
    }

}
