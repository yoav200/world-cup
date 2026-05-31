package com.ab.worldcup.web.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionManager implements HandlerInterceptor {

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
