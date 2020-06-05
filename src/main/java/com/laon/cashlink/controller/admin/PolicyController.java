package com.laon.cashlink.controller.admin;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.dto.policy.PolicyDTO;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.admin.policy.AdminPolicyService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("AdminPolicyController")
@RequestMapping("/api/admin/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final AdminPolicyService policyService;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> readPolicyList(
        @RequestParam(required = false, defaultValue = "0") Long page,
        HttpServletResponse res
                                             ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", policyService.readPolicyList(page));
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

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> createPolicy(
        @RequestBody @Valid PolicyDTO.Request request,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                           ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.SC_CREATED);
            returnMap.put("result", 1);
            returnMap.put("data", policyService.createPolicy(request, user));
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

    @RequestMapping(method = RequestMethod.PATCH)
    public Map<String, Object> updatePolicy(
        @RequestBody @Valid PolicyDTO.Request request,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                           ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", policyService.updatePolicy(request, user));
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
