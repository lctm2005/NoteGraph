package com.licong.notemap.web.security;

import com.licong.notemap.service.LoginService;
import com.licong.notemap.util.JsonUtils;
import com.licong.notemap.util.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.scribe.model.Token;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Setter
public class EvernoteAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private LoginService loginService;

    public EvernoteAuthenticationFilter() {
        super(new AntPathRequestMatcher("/accessToken"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (null == request.getSession()) {
            log.debug(request.getRequestURL() + "session is null");
            return null;
        }
        HttpSession session = request.getSession();
        Token requestToken = (Token) request.getSession().getAttribute("requestToken");
        if (null == requestToken) {
            throw new AuthenticationServiceException("requestToken is null");
        }
        String oauthVerifier = request.getParameter("oauth_verifier");
        // 取回 Access Token
        if (StringUtils.isEmpty(oauthVerifier)) {
            throw new AuthenticationServiceException("oauthVerifier is empty");
        }
        Token accessToken = loginService.getAccessToken(requestToken, oauthVerifier, request.getRequestURL().toString());
//        session.setAttribute("accessToken", accessToken);

        Authentication authentication = new EvernoteAuthentication(accessToken);
        log.debug(request.getRequestURL() + "提取认证信息成功, accessToken:" + JsonUtils.toJson(accessToken));
        return this.getAuthenticationManager().authenticate(authentication);
    }

}
