package com.laon.cashlink.common.config;

import com.laon.cashlink.common.lib.CustomPrincipalExtractor;
import io.micrometer.core.instrument.util.IOUtils;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@RequiredArgsConstructor
class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomPrincipalExtractor principalExtractor;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable();

        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/api/users/**").authenticated()
                    .antMatchers("/api/admin/**").hasRole("ADMIN")
                    .antMatchers("/api/markets", "/api/markets/info").permitAll()
                    .antMatchers("/api/infos/**").permitAll()
                    .antMatchers("/api/notices").permitAll()
                    .antMatchers("/api/faq").permitAll()
                    .anyRequest().authenticated();
        //@formatter:on
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        ClassPathResource resource = new ClassPathResource("public.key");
        String publicKey;
        try {
            publicKey = IOUtils.toString(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        converter.setVerifierKey(publicKey);

        return converter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(customAuthenticationEntryPoint);
        resources.accessDeniedHandler(customAccessDeniedHandler);
    }

    @Bean
    public PrincipalExtractor principalExtractor() {
        return principalExtractor;
    }

}