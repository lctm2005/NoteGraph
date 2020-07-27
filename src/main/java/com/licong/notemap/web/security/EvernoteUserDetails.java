package com.licong.notemap.web.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class EvernoteUserDetails implements UserDetails {

    private EvernoteAccessToken evernoteAccessToken;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return evernoteAccessToken.getSecret();
    }

    @Override
    public String getUsername() {
        return evernoteAccessToken.getUserId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return evernoteAccessToken.expired();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
