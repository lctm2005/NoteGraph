package com.licong.notemap.web.security.evernote;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class EvernoteAuthentication extends AbstractAuthenticationToken {

    private EvernoteUserDetails principal;
    private EvernoteAccessToken credentials;

    public EvernoteAuthentication(EvernoteAccessToken credentials) {
        super(null);
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public EvernoteAuthentication(EvernoteUserDetails principal, EvernoteAccessToken credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }

    @Override
    public EvernoteUserDetails getPrincipal() {
        return principal;
    }

    @Override
    public EvernoteAccessToken getCredentials() {
        return credentials;
    }

}
