package com.licong.notemap.web.security;

import com.licong.notemap.service.LoginService;
import com.licong.notemap.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AccessDeniedHandler 用来解决认证过的用户访问无权限资源时的异常
 */
@Slf4j
@Component
public class EvernoteAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private LoginService loginService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 请求临时Token
        log.debug("请求临时Token");
        Token requestToken = loginService.getRequestToken("http://localhost:8080/accessToken");
        log.debug("临时Token:" + JsonUtils.toJson(requestToken));
        request.getSession().setAttribute("requestToken", requestToken);

        // 请求用户认证
        log.debug("请求用户认证");
        response.sendRedirect(loginService.getUserOAuth(requestToken));
    }
}
