package com.laon.cashlink.controller;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

import com.laon.cashlink.common.define.user.NotiStatus;
import com.laon.cashlink.common.define.user.NotiSubType;
import com.laon.cashlink.common.define.user.NotiType;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.dto.Remit;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.user.UserService;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> searchUser(@RequestParam String type, @RequestParam String query,
                                          HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.searchUser(type, query));
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

    /**
     * 유저 정보 가져오는 API
     */
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Map<String, Object> readMyInfo(@AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.readMyInfo(user.getUsername()));
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

    /**
     * 유저의 계좌를 가져오는 API
     */
    @RequestMapping(value = "/me/account", method = RequestMethod.GET)
    public Map<String, Object> readMyAccount(@AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.readMyAccount(user));
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

    @RequestMapping(value = "/me/account/remit", method = RequestMethod.POST)
    public Map<String, Object> remit(@AuthenticationPrincipal User user, @RequestBody Remit.Request request,
                                     HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.remit(user, request));
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

    @RequestMapping(value = "/me/account/{account_id:[0-9A-Z]{9}}/tx", method = RequestMethod.GET)
    public Map<String, Object> readAccountTx(
            @PathVariable String account_id,
            @RequestParam(required = false, defaultValue = "0") Long page, @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "") String duration,
            @RequestParam(required = false, defaultValue = "") String status,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.readAccountTx(account_id, page, user, duration, status));
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

    /**
     * 유저의 알림 가져오는 API
     */
    @RequestMapping(value = "/me/noti", method = RequestMethod.GET)
    public Map<String, Object> readMyNotification(@AuthenticationPrincipal User user,
                                                  @RequestParam(required = false, defaultValue = "0") Long page,
                                                  @RequestParam(required = false) NotiType type,
                                                  @RequestParam(required = false, defaultValue = "NOT_READ") NotiStatus status,
                                                  @RequestParam(required = false) NotiSubType sub_type, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.readMyNotification(user, page, type, sub_type, status));
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

    /**
     * 안 읽은 알림 확인
     */
    @RequestMapping(value = "/me/noti/unread", method = RequestMethod.GET)
    public Map<String, Object> checkUnReadNoti(@AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.checkUnReadNoti(user));
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

    /**
     * 알림 하나 읽음 처리 API
     */
    @RequestMapping(value = "/me/noti/{noti_id:[0-9]+}", method = RequestMethod.POST)
    public Map<String, Object> readMyNoti(@AuthenticationPrincipal User user, @PathVariable Long noti_id,
                                          HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.CREATED.value());
            returnMap.put("result", 1);
            returnMap.put("data", userService.readMyNoti(user, noti_id));
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

    /**
     * 알림 하나 삭제 API
     */
    @RequestMapping(value = "/me/noti/{noti_id:[0-9]+}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteMyNoti(@AuthenticationPrincipal User user, @PathVariable Long noti_id,
                                            HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.deleteMyNoti(user, noti_id));
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

    /**
     * 알림 전체 읽음 처리 API
     */
    @RequestMapping(value = "/me/noti/all", method = RequestMethod.POST)
    public Map<String, Object> readMyNotiAll(@AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.CREATED.value());
            returnMap.put("result", 1);
            returnMap.put("data", userService.readMyNotiAll(user));
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

    @RequestMapping(value = "/me/token", method = RequestMethod.PUT)
    public Map<String, Object> updateUserToken(@AuthenticationPrincipal User user, @RequestParam String token,
                                               HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.updateUserToken(user, token));
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

    /**
     * 유저 데이터 싱크 맞추는 API
     */
    @RequestMapping(value = "/me/sync", method = RequestMethod.POST)
    public Map<String, Object> syncUser(Principal principal, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.syncUser(principal));
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

    @RequestMapping(value = "/me/market", method = RequestMethod.GET)
    public Map<String, Object> readMyMarketStatus(@AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.readMyMarketStatus(user));
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

    /**
     * 유저가 구매 신청 한 내역을 확인하는 API
     */
    @RequestMapping(value = "/me/purchases", method = RequestMethod.GET)
    public Map<String, Object> readMyPurchases(@AuthenticationPrincipal User user,
                                               @RequestParam(required = false, defaultValue = "0") Long page, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.readMyPurchases(user, page));
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


    /*
     * 유저 PIN 비밀번호 관련 API
     * */

    @RequestMapping(value = "/me/pin/duplicate", method = RequestMethod.GET)
    public Map<String, Object> duplicatePinPass(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.duplicatePinPass(user,password));
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

    @RequestMapping(value = "/me/pin", method = RequestMethod.PATCH)
    public Map<String, Object> updatePinPass(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", userService.updatePinPass(user,password));
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