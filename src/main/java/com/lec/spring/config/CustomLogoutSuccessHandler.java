package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LocalDateTime localDateTime = LocalDateTime.now();

        LocalDateTime logoutTime = LocalDateTime.now();

        LocalDateTime loginTime = (LocalDateTime) request.getSession().getAttribute("loginTime");

        if(loginTime != null) {
            long seconds = loginTime.until(logoutTime, ChronoUnit.SECONDS);
        }

        request.getSession().invalidate();

        String redirectUrl = "/member/login?logoutHandler";

        if(request.getParameter("ret_url") != null) {
            redirectUrl = request.getParameter("ret_url");
        }

        response.sendRedirect(redirectUrl);
    }
}
