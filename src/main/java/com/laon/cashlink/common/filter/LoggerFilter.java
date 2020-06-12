/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.laon.cashlink.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.laon.cashlink.common.lib.ResponseWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

@Slf4j
public class LoggerFilter implements Filter {

    private ObjectMapper om = new ObjectMapper();

    public LoggerFilter() {
        om.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
//        if ("/sbp_api/api/ Finfos/privacy".equals(((HttpServletRequest) req).getRequestURI())) {
//            chain.doFilter(req, res);
//            return;
//        }
        HttpServletResponse resp = (HttpServletResponse) res;
        ResponseWrapper response = new ResponseWrapper(resp);
        response.setHeader("Content-Security-Policy", "script-src 'self'");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-XSS-Protection", "1;mode=block");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        String URI = ((HttpServletRequest) req).getRequestURI();
        String METHOD = "[" + ((HttpServletRequest) req).getMethod() + "]";
        String IP = ((HttpServletRequest) req).getHeader("x-real-ip");
        if (StringUtils.isEmpty(IP)) {
            IP = req.getRemoteAddr();
        }
        String USER_AGENT = ((HttpServletRequest) req).getHeader("User-Agent");
        long time = System.currentTimeMillis();

        try {
            log.info("USER-AGENT: {}", USER_AGENT);
            log.info("{} REQUEST {} {} --> ", METHOD, URI, IP);
            log.info("CONTENT-TYPE: {}", req.getContentType());
            // TODO: Content-Type에 따라서 Request 로그 바꾸기/ application/json일때 로그 안나옴
            log.info("\nHTTP REQUEST PARAMETER\n{} ", om.writeValueAsString(req.getParameterMap()));

            chain.doFilter(req, response);
        } finally {
            response.flushBuffer();
            byte[] copy = response.getCopy();

            String responseBody = new String(copy, StandardCharsets.UTF_8);
            JSONObject body;
            try {
                if (!StringUtils.isEmpty(responseBody)) {
                    body = new JSONObject(responseBody);
                    log.info("\nSTATUS CODE: {}\nHTTP RESPONSE:\n{}", response.getStatus(), body.toString(2));
                }
            } catch (Exception e) {
                log.error("RESPONSE PARSE ERROR, ERR_MSG: {}, RAW: {}", e, responseBody);
            }
            time = System.currentTimeMillis() - time;
            log.info("{} REQUEST {} {} <-- {}ms", METHOD, URI, IP, time);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // log.debug("################### log FILTER INIT ###################");
    }

    @Override
    public void destroy() {
        // log.debug("################### log FILTER END. ###################");
    }

}