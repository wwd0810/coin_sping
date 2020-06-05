package com.laon.cashlink.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laon.cashlink.common.define.ApiErrorCode;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper om;

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
        throws IOException {
        log.debug("", e);
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setCharacterEncoding("utf-8");
        Map<String, Object> returnMap = new HashMap<>();
        {
            returnMap.put("result", 0);
            returnMap.put("resultCode", ApiErrorCode.CREDENTIALS_IS_INVALID.getCode());
            returnMap.put("resultMsg", ApiErrorCode.CREDENTIALS_IS_INVALID.getMsg());
        }

        OutputStream os = res.getOutputStream();
        om.writeValue(os, returnMap);
        os.flush();
    }

}