package com.example.scheduler.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        
        // 회원가입, 로그인 요청은 인증 처리에서 제외
        if (requestURI.equals("/api/users") || requestURI.equals("/api/users/login")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 세션에서 인증 정보 확인
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("인증이 필요합니다.");
            return;
        }
        
        // 인증된 사용자는 요청 계속 진행
        chain.doFilter(request, response);
    }
}
