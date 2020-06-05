package com.laon.cashlink.common.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.BigDecimalSerializer;
import com.laon.cashlink.common.lib.EnumSerializer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper om() {
        List<Module> modules = new ArrayList<>();

        SimpleModule sm = new SimpleModule();
        sm.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        sm.addSerializer(CodeEnum.class, new EnumSerializer());

        modules.add(sm);
        modules.add(new JavaTimeModule());

        ObjectMapper om = Jackson2ObjectMapperBuilder
            .json()
            .modules(modules)
            .build();

        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return om;
    }

}