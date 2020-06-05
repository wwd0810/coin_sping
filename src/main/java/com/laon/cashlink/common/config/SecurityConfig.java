package com.laon.cashlink.common.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Order(1)
@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) {
    List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();
    {
//        matchers.add(new AntPathRequestMatcher("/api/markets"));
        matchers.add(new AntPathRequestMatcher("/api/markets/info"));
    }

    http
    .requestMatcher(new OrRequestMatcher(matchers));
//                .authorizeRequests()
//                .antMatchers("/api/markets").permitAll()
    }

    }