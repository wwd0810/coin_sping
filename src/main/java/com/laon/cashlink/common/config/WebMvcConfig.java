package com.laon.cashlink.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Order(1)
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

}
