package com.ab.worldcup.web.components;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionManager extends HandlerInterceptorAdapter {

    public static final long MAX_INACTIVE_SESSION_TIME = 1000 * 60 * 20; // 20 minutes

    public static final String LAST_REQUEST_TIME = "lastRequestTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(LAST_REQUEST_TIME, System.currentTimeMillis());
        }
        return true;
    }
}
