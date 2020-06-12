package com.laon.cashlink.controller.admin;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.dto.faq.FaqDTO;

import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.admin.faq.AdminFaqService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

@Slf4j
@RestController("AdminFaqController")
@RequestMapping("/api/admin/faq")
@RequiredArgsConstructor
public class AdminFaqController {

    private final AdminFaqService adminFaqService;

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> createFaq(
            @RequestBody @Valid FaqDTO.Request request,
            @AuthenticationPrincipal User user,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try{
            returnMap.put("result", 1);
            returnMap.put("data", adminFaqService.createFaq(user, request));
        } catch (ApiException e) {
            res.setStatus(e.getStatus());
            returnMap.put("result", 0);
            returnMap.put("resultCode", e.getCode());
            returnMap.put("resultMsg", e.getMsg());
        }catch (Exception e) {
            log.error("", e);
            res.setStatus(500);
            returnMap.put("result", 0);
            returnMap.put("resultCode", UNKNOWN.getCode());
            returnMap.put("resultMsg", UNKNOWN.getMsg());
        }

        return  returnMap;
    }

    @RequestMapping(value = "/{faq_id:[0-9]+}", method = RequestMethod.GET)
    public  Map<String, Object> readFaq (
            @AuthenticationPrincipal User user,
            @PathVariable Long faq_id,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminFaqService.readFaq(user, faq_id));
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

    @RequestMapping(value = "/{faq_id:[0-9]+}", method = RequestMethod.PATCH)
    public Map<String, Object> updateFaq(
            @PathVariable Long faq_id,
            @RequestBody FaqDTO.Request request,
            @AuthenticationPrincipal User user,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminFaqService.updateFaq(user, faq_id, request));
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

    @RequestMapping(value = "/{faq_id:[0-9]+}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteFaq(
            @PathVariable Long faq_id,
            @AuthenticationPrincipal User user,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminFaqService.deleteFaq(user, faq_id));
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
