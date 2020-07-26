package com.licong.notemap.web.security;

import org.scribe.model.Token;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class EvernoteAuthentication extends AbstractAuthenticationToken {

    private final Object principal;

    public EvernoteAuthentication(Token accessToken) {
        super(null);
        this.principal = accessToken;
        this.setAuthenticated(false);
    }

    public EvernoteAuthentication(Object principal,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
