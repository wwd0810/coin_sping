package com.laon.cashlink.controller.admin;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.dto.notice.NoticeDTO;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.admin.notice.AdminNoticeService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.laon.cashlink.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("AdminNoticeController")
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final AdminNoticeService noticeService;

    private final NoticeService normalNoticeService;

    @Cacheable(cacheNames = "notices", key = "#page")
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> readNoticeList(
            @RequestParam(required = false, defaultValue = "0") Long page,
            @RequestParam(required = false) String query,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", normalNoticeService.readNoticeList(page, query));
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

    @RequestMapping(value = "/{notice_id:[0-9]+}", method = RequestMethod.GET)
    public Map<String, Object> readNotice (
            @AuthenticationPrincipal User user,
            @PathVariable Long notice_id,
            HttpServletResponse res
            )
    {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", noticeService.readNotice(user, notice_id));
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

    @CacheEvict(cacheNames = "notices", allEntries = true)
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> createNotice(
        @RequestBody @Valid NoticeDTO.Request request,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                           ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.SC_CREATED);
            returnMap.put("result", 1);
            returnMap.put("data", noticeService.createNotice(request, user));
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

    @RequestMapping(value = "/{notice_id:[0-9]+}", method = RequestMethod.PATCH)
    public Map<String, Object> updateNotice(
        @PathVariable Long notice_id,
        @RequestBody NoticeDTO.Request request,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                           ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", noticeService.updateNotice(notice_id, request, user));
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

    @RequestMapping(value = "/{notice_id:[0-9]+}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteNotice(
        @PathVariable Long notice_id,
        HttpServletResponse res
                                           ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", noticeService.deleteNotice(notice_id));
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
