package com.laon.cashlink.controller.admin;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.admin.user.AdminUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

@Slf4j
@RestController("AdminUserController")
//@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;
    /**
     * 관리자 유저리스트 API
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> readUserList(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "0") Long page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "RECENT|DESC") String order,
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false) String duration,
            HttpServletResponse res
    ) {
        Map<String, Object> returnMap = new HashMap<>();
        log.info("user: {}", user);

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminUserService.readUserList(user, page, size, order, query, duration));

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
