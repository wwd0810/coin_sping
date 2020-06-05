package com.laon.cashlink.controller;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.info.InfoService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.lang.Exception;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/infos")
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    /**
     * 앱 필수 버전 및 최신 버전을 가져오는 API
     */
    @Cacheable(value = "application")
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    public Map<String, Object> readApplicationInfo(HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", infoService.readApplicationInfo());
        } catch (ApiException e) {
            res.setStatus(e.getStatus());
            returnMap.put("result", 0);
            returnMap.put("resultCode", e.getCode());
            returnMap.put("resultMsg", e.getMsg());
        } catch (Exception e) {
            log.error("", e);
            res.setStatus(500);
            returnMap.put("result", 0);
            returnMap.put("resultCode", UNKNOWN.getCode());
            returnMap.put("resultMsg", UNKNOWN.getMsg());
        }

        return returnMap;
    }

    @Cacheable(value = "terms", key = "#type")
    @RequestMapping(value = "/terms/{type}", method = RequestMethod.GET)
    public Map<String, Object> readTerms(
        @PathVariable String type,
        HttpServletResponse res
                                        ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", infoService.readTerms(type));
        } catch (ApiException e) {
            res.setStatus(e.getStatus());
            returnMap.put("result", 0);
            returnMap.put("resultCode", e.getCode());
            returnMap.put("resultMsg", e.getMsg());
        } catch (Exception e) {
            log.error("", e);
            res.setStatus(500);
            returnMap.put("result", 0);
            returnMap.put("resultCode", UNKNOWN.getCode());
            returnMap.put("resultMsg", UNKNOWN.getMsg());
        }

        return returnMap;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public User test(
        @AuthenticationPrincipal User user
                                   ) {
        return user;
    }

}