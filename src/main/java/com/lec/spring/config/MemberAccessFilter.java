package com.lec.spring.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberAccessFilter extends OncePerRequestFilter {

    private static final Pattern MEMBER_URL_PATTERN = Pattern.compile("/members/(\\d+)");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        Matcher matcher = MEMBER_URL_PATTERN.matcher(path);

        if (matcher.matches()) {
            Long requestedMemberId = Long.parseLong(matcher.group(1));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

                boolean isSameUser = principalDetails.getMember().getId().equals(requestedMemberId);

                if (!isAdmin && !isSameUser) {
                    response.setContentType("text/html; charset=UTF-8");
                    PrintWriter out = response.getWriter();

                    out.println("<script>");
                    out.println("alert('접근에 필요한 권한이 없습니다. 이전 페이지로 돌아갑니다.');");
                    out.println("history.back();");
                    out.println("</script>");

                    out.flush();
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}