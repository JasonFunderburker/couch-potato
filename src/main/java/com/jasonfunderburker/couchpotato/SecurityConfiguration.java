package com.jasonfunderburker.couchpotato;

import com.jasonfunderburker.couchpotato.security.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Autowired
    public SecurityConfiguration(DataSource dataSource, UserDetailsService userDetailsService, RestAuthenticationEntryPoint entryPoint) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.entryPoint = entryPoint;
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
                .httpBasic()
                    .authenticationEntryPoint(entryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.jdbcAuthentication().dataSource(dataSource);
    }
}
