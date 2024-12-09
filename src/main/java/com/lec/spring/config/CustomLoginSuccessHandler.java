package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        LocalDateTime loginTime = LocalDateTime.now();
        request.getSession().setAttribute("loginTime", loginTime);

        clearAuthenticationAttributes(request);

        if (authentication instanceof OAuth2AuthenticationToken) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            // 새로운 OAuth 사용자만 additional-info로, 나머지는 홈으로
            if (principalDetails.isNewOAuthUser()) {
                getRedirectStrategy().sendRedirect(request, response, "/member/additional-info");
                return;
            }

            // 기존 OAuth 사용자는 홈으로
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        // OAuth2가 아니거나 추가 정보가 필요없는 경우
        super.onAuthenticationSuccess(request, response, authentication);
    }

    // request 를 한 client ip 가져오기
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}