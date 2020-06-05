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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper om;

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException {
        log.debug("", e);
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> returnMap = new HashMap<>();
        {
            returnMap.put("result", 0);
            returnMap.put("resultCode", ApiErrorCode.NOT_AUTHORIZED.getCode());
            returnMap.put("resultMsg", ApiErrorCode.NOT_AUTHORIZED.getMsg());
        }

        OutputStream os = res.getOutputStream();
        om.writeValue(os, returnMap);
        os.flush();
    }

}