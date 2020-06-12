package com.laon.cashlink.controller.admin;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.dto.Remit;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.admin.inquiry.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

@Slf4j
@RestController("AdminInquiryController")
@RequestMapping("/api/admin/inquiry")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> readInquiryList(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "RECENT|DESC") String order,
            @RequestParam(required = false, defaultValue = "0") Long page,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminInquiryService.readAdminInquiry(query,page, size, order, status));
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

    @RequestMapping(value = "/{inquiry_id:[0-9]+}", method = RequestMethod.GET)
    public Map<String, Object> readInquiry(

            @AuthenticationPrincipal User user,
            @PathVariable Long inquiry_id,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminInquiryService.readInquiry(user, inquiry_id));
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

    @RequestMapping(value = "/{inquiry_id:[0-9]+}" ,method =RequestMethod.PATCH)
    public Map<String, Object> updateInquiry(
            @AuthenticationPrincipal User user,
            @PathVariable Long inquiry_id,
            @RequestParam String answer,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminInquiryService.updateInquiry(user, inquiry_id, answer));
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

    @RequestMapping(value = "/{inquiry_id:[0-9]+}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteInquiry (
            @AuthenticationPrincipal User user,
            @PathVariable Long inquiry_id,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminInquiryService.deleteInqiury(user, inquiry_id));
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
