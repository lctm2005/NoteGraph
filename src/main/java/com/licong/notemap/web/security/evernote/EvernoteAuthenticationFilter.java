package com.licong.notemap.web.security.evernote;

import com.evernote.edam.type.Notebook;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Setter
public class EvernoteAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private EvernoteLoginService evernoteLoginService;

    public EvernoteAuthenticationFilter() {
        super(new AntPathRequestMatcher(EvernoteAuthenticationConstant.ACCESS_TOKEN_URI));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        EvernoteAccessToken evernoteAccessToken = EvernoteAccessToken.DEVELOP_TOKEN;
//        try {

        HttpSession session = request.getSession();
        if (null == session) {
            log.debug(request.getRequestURL() + "session is null");
            return null;
        }
        Token requestToken = (Token) session.getAttribute("requestToken");
        if (null == requestToken) {
            throw new AuthenticationServiceException("requestToken is null");
        }
        String oauthVerifier = request.getParameter(EvernoteAuthenticationConstant.OAUTH_VERIFIER_PARAM);
        // 取回 Access Token
        if (StringUtils.isEmpty(oauthVerifier)) {
            throw new AuthenticationServiceException("oauthVerifier is empty");
        }
        Token accessToken = evernoteLoginService.getAccessToken(requestToken, oauthVerifier, request.getRequestURL().toString());
        EvernoteAccessToken evernoteAccessToken = new EvernoteAccessToken(accessToken);
        log.debug(request.getRequestURL() + "提取认证信息成功, accessToken:" + JsonUtils.toJson(accessToken));

        // 获得授权的笔记本
        Notebook notebook = evernoteLoginService.getAuthorizedNotebook(evernoteAccessToken.getNoteStoreUrl(), evernoteAccessToken.getToken());
        evernoteAccessToken.setNotebook(notebook);
//        } catch (Exception e) {
//            log.error("认证异常，降级到开发者账号", e);
//        }
        EvernoteAuthentication authentication = new EvernoteAuthentication(evernoteAccessToken);
        return this.getAuthenticationManager().authenticate(authentication);
    }


}
