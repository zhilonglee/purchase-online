package com.zhilong.springcloud.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    private SessionContext sessionContext = SessionContext.getInstance();

    private final Logger logger = LoggerFactory.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // HttpSession session = se.getSession();
        // logger.info("create session id : " + session.getId());
        // sessionContext.addSession(session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        logger.info("destroy session id : " + session.getId());
        sessionContext.delSession(session);
    }
}
