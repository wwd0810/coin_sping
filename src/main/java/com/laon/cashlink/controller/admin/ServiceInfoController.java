package com.laon.cashlink.controller.admin;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.dto.common.ApplyServiceInfo;
import com.laon.cashlink.entity.dto.notice.NoticeDTO;
import com.laon.cashlink.service.admin.serviceInfo.ServiceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

@Slf4j
@RestController("ServiceInfoController")
@RequestMapping("/api/admin/service-info")
@RequiredArgsConstructor
public class ServiceInfoController {

    private final ServiceInfoService serviceInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> readServiceInfo(
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", serviceInfoService.readServiceInfo());
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
    public Map<String, Object> updateServiceInfo(
            @RequestBody @Valid ApplyServiceInfo.Request request,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", serviceInfoService.updateSerticeInfo(request));
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
