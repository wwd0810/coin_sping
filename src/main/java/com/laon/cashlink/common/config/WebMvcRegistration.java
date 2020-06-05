package com.laon.cashlink.common.config;

import com.laon.cashlink.common.filter.LoggerFilter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebMvcRegistration implements WebMvcRegistrations {

    @Bean
    public FilterRegistrationBean<LoggerFilter> loggerFilter() {
        FilterRegistrationBean<LoggerFilter> bean = new FilterRegistrationBean<>(new LoggerFilter());
        bean.addUrlPatterns("/api/*");
        return bean;
    }

}
