package com.laon.cashlink.controller;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.faq.FaqService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaQController {

    private final FaqService faqService;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> readFaqList(
            @RequestParam(required = false) String query,
        HttpServletResponse res
                                          ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", faqService.readFaqList(query));
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

}
