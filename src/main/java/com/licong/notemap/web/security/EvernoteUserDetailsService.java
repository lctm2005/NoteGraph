package com.licong.notemap.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EvernoteUserDetailsService implements UserDetailsService {


    public EvernoteUserDetailsService() {
        log.info("CustomUserDetailsService loading success ...");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username:" + username);
        return new User(username, "admin",
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
    }
}
