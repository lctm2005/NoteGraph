package com.licong.notemap.web.security.evernote;

import com.licong.notemap.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import static com.licong.notemap.web.security.evernote.EvernoteAuthenticationConstant.LOGIN_SERVICE_NAME;

//@EnableWebSecurity
public class EvernoteSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private EvernoteAuthenticationEntryPoint evernoteAuthenticationEntryPoint;

    @Autowired
    private EvernoteAuthenticationFailureHandler evernoteAuthenticationFailureHandler;

    /**
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        EvernoteAuthenticationFilter evernoteAuthenticationFilter = new EvernoteAuthenticationFilter();
        evernoteAuthenticationFilter.setAuthenticationManager(authenticationManager());
        evernoteAuthenticationFilter.setAuthenticationFailureHandler(evernoteAuthenticationFailureHandler);
        evernoteAuthenticationFilter.setEvernoteLoginService((EvernoteLoginService)SpringContextUtil.getBean(LOGIN_SERVICE_NAME));
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(evernoteAuthenticationFilter, ExceptionTranslationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(evernoteAuthenticationEntryPoint)
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new EvernoteAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) {
        //解决静态资源被拦截的问题
        web.ignoring()
                .antMatchers("/favicon.ico");
    }

}
