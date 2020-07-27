package com.licong.notemap.web.security;

import com.licong.notemap.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
 * https://blog.csdn.net/jkjkjkll/article/details/79975975
 */
@Slf4j
@Component
public class EvernoteAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private EvernoteLoginService evernoteLoginService;

    /**
     * 开启一次认证流程
     *
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 请求临时Token
        log.debug("请求临时Token");
        Token requestToken = evernoteLoginService.getRequestToken(
                request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length())
                        + EvernoteAuthenticationConstant.ACCESS_TOKEN_URI);
        log.debug("临时Token:" + JsonUtils.toJson(requestToken));
        request.getSession().setAttribute("requestToken", requestToken);

        // 请求用户认证
        log.debug("请求用户认证");
        response.sendRedirect(evernoteLoginService.getUserOAuth(requestToken));
    }
}
