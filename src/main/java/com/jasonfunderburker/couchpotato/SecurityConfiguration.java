package com.jasonfunderburker.couchpotato;

import com.jasonfunderburker.couchpotato.security.SingleUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

    @Autowired
    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/**", "/register").permitAll()
                    .antMatchers(HttpMethod.GET, "/rss/public/?*").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin().failureUrl("/login?error")
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsManager()).passwordEncoder(new BCryptPasswordEncoder());
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    @Bean
    public SingleUserDetailsManager userDetailsManager() {
        SingleUserDetailsManager userDetailsManager = new SingleUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        return userDetailsManager;
    }
}
