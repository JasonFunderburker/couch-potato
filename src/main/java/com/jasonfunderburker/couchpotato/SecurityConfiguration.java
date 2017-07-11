package com.jasonfunderburker.couchpotato;

import com.jasonfunderburker.couchpotato.security.RestAuthenticationEntryPoint;
import com.jasonfunderburker.couchpotato.security.RestAuthenticationFailureHandler;
import com.jasonfunderburker.couchpotato.security.RestAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Created by JasonFunderburker on 25.02.17.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;
    private final RestAuthenticationEntryPoint entryPoint;
    private final RestAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationFailureHandler failureHandler;

    @Autowired
    public SecurityConfiguration(DataSource dataSource, UserDetailsService userDetailsService, RestAuthenticationEntryPoint entryPoint, RestAuthenticationSuccessHandler successHandler, RestAuthenticationFailureHandler failureHandler) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.entryPoint = entryPoint;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/css/**", "/register").permitAll()
                    .antMatchers(HttpMethod.GET, "/rss/public/?*").permitAll()
                    .antMatchers(HttpMethod.GET, "/checkResults/download/torrent?*.torrent/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(entryPoint)
                    .and()
                .formLogin()
                    .successHandler(successHandler)
                    .failureHandler(failureHandler);
                /*.formLogin().failureUrl("/login?error")
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll(); */
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.jdbcAuthentication().dataSource(dataSource);
    }
}
