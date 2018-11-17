package com.zhilong.springcloud.listener;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionContext {

    private static SessionContext instance;
    /**
     * ConcurrentHashMap replae HashMap to resolve {@link ConcurrentModificationException}
     */
    private ConcurrentHashMap<String, HttpSession> sessionMap;

    private SessionContext() {
        sessionMap = new ConcurrentHashMap<String,HttpSession>();
    }

    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    public synchronized void addSession(HttpSession session) {
        if (session != null) {
            sessionMap.put(session.getId(), session);
        }
    }

    public synchronized void delSession(HttpSession session) {
        if (session != null) {
            sessionMap.remove(session.getId());
        }
    }

    public synchronized HttpSession getSession(String sessionID) {
        if (sessionID == null) {
            return null;
        }
        return sessionMap.get(sessionID);
    }

    public synchronized Set<String> getSessionIdSet() {
        Set<String> sessionSet = null;
        if (instance != null) {
           sessionSet = sessionMap.keySet();
        }
        return sessionSet;
    }

}
