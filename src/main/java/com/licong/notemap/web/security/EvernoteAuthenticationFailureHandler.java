package com.licong.notemap.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EvernoteAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private EvernoteAuthenticationEntryPoint evernoteAuthenticationEntryPoint;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 如果认证失败就再发起一次认证
        evernoteAuthenticationEntryPoint.commence(request, response, exception);
    }
}
