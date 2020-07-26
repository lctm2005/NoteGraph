package com.licong.notemap.web.security;

import com.licong.notemap.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class EvernoteAuthenticationProvider implements AuthenticationProvider {


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // TODO 获取用户信息
        UserDetails userDetails = new User(authentication.getPrincipal().toString(),
                "",
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
        log.debug("获取用户信息:" + JsonUtils.toJson(userDetails));
        // TODO 处理过期
        EvernoteAuthentication authenticationResult = new EvernoteAuthentication(userDetails, userDetails.getAuthorities());
        log.debug("认证通过:" + JsonUtils.toJson(authenticationResult));
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EvernoteAuthentication.class.isAssignableFrom(aClass);
    }
}
