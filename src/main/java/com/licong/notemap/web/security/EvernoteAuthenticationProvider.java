package com.licong.notemap.web.security;

import com.licong.notemap.repository.evernote.EvernoteRepository;
import com.licong.notemap.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

@Slf4j
@AllArgsConstructor
public class EvernoteAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        EvernoteAuthentication evernoteAuthentication = (EvernoteAuthentication) authentication;
        EvernoteAccessToken evernoteAccessToken = evernoteAuthentication.getCredentials();

        EvernoteUserDetails userDetails = new EvernoteUserDetails(evernoteAccessToken,
                AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));

        EvernoteAuthentication authenticationResult = new EvernoteAuthentication(userDetails, evernoteAccessToken);
        log.debug("认证通过:" + JsonUtils.toJson(authenticationResult));
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return EvernoteAuthentication.class.isAssignableFrom(aClass);
    }
}
