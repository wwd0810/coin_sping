package com.laon.cashlink.controller.admin;

import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.service.admin.market.AdminMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

@Slf4j
@RestController("AdminMarketController")
@RequestMapping("/api/admin/market")
@RequiredArgsConstructor
public class AdminMarketController {

    private final AdminMarketService adminMarketService;

    @RequestMapping(value = "/purchases" , method = RequestMethod.GET)
    public Map<String, Object> readDealList (
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "0") Long page,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) Integer quanLow,
            @RequestParam(required = false) Integer quanHigh,
            @RequestParam(required = false) Integer priceLow,
            @RequestParam(required = false) Integer priceHigh,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminMarketService.readDealList(query, page, duration, quanLow, quanHigh, priceLow, priceHigh));
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

    @RequestMapping(value = "/product" , method = RequestMethod.GET)
    public Map<String, Object> readProductList (
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "0") Long page,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) Integer quanLow,
            @RequestParam(required = false) Integer quanHigh,
            @RequestParam(required = false) Integer priceLow,
            @RequestParam(required = false) Integer priceHigh,
            HttpServletResponse res
    ) {

        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminMarketService.readProductList(query, page, duration, quanLow, quanHigh, priceLow, priceHigh));
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

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Map<String, Object> countDeals (HttpServletResponse res){
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", adminMarketService.countDeals());
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
