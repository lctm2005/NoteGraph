package com.licong.notemap.web.security.inmemory;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .formLogin(Customizer.withDefaults())
                .authorizeRequests()
                .antMatchers("/").hasRole("USER")
                .antMatchers("/api/**").hasRole("USER")
                .antMatchers("/error").permitAll()
                .antMatchers("/login").permitAll()
                .and()
                .formLogin().loginPage("/login").permitAll();
//                .and()
//                .anonymous().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/favicon.ico");
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("lctm2005").password("Luvy7*GovuPafi").roles("USER");
//    }

    @Bean
    public UserDetailsService users() {
        // The builder will ensure the passwords are encoded before saving in memory
        User.UserBuilder users = User.builder();
        UserDetails admin = users
                .username("lctm2005")
                .password("{bcrypt}$2a$10$kTfSO1C0sZOvBnBxTX8VRuHi2aRY1hjn1tjK8js6RaP1eW9KO/Owm") //Luvy7*GovuPafi
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }


}
