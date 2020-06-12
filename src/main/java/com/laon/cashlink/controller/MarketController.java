package com.laon.cashlink.controller;

import static com.laon.cashlink.common.define.ApiErrorCode.UNKNOWN;

import com.laon.cashlink.common.define.market.MarketStatus;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.dto.market.MarketSale;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.service.market.MarketService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/markets")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    /**
     * 유저가 판매 신청한 DL을 확인하는 API
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> readMarketList(@AuthenticationPrincipal User user,
        @RequestParam(required = false, defaultValue = "0") Long page,
        @RequestParam(required = false, defaultValue = "10") Integer size,
        @RequestParam(required = false, defaultValue = "RECENT|DESC") String order,
        @RequestParam(required = false, defaultValue = "") String query, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();
        log.info("user: {}", user);

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.readMarketList(user, page, size, order, query));
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
     * 유저가 판매 신청한 DL을 하나만 확인하는 API
     */
    @RequestMapping(value = "/{market_id:[0-9]+}", method = RequestMethod.GET)
    public Map<String, Object> readMarket(@PathVariable Long market_id, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.readMarket(market_id));
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

    @RequestMapping(value = "/{market_id:[0-9]+}", method = RequestMethod.PATCH)
    public Map<String, Object> updateMarket(@PathVariable Long market_id,
        @RequestBody MarketSale.Request request,
        @AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.updateMarket(market_id, request, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}", method = RequestMethod.DELETE)
    public Map<String, Object> cancelPurchase(@AuthenticationPrincipal User user,
        @PathVariable Long market_id,
        HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.cancelPurchase(user, market_id));
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
     * DL 구매 요청 API
     */
    @RequestMapping(value = "/{market_id:[0-9]+}/buy", method = RequestMethod.POST)
    public Map<String, Object> createPurchaseRequest(@AuthenticationPrincipal User user,
        @PathVariable Long market_id,
        HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.CREATED.value());
            returnMap.put("result", 1);
            returnMap.put("data", marketService.createPurchaseRequest(user, market_id));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases", method = RequestMethod.GET)
    public Map<String, Object> readPurchaseList(
        @PathVariable Long market_id,
        @RequestParam(required = false, defaultValue = "0") Long page,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                               ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.readPurchaseList(market_id, page, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases/{purchase_id:[0-9]+}", method = RequestMethod.GET)
    public Map<String, Object> readPurchase(
        @PathVariable Long market_id,
        @PathVariable Long purchase_id,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                           ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.readPurchase(market_id, purchase_id, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases/{purchase_id:[0-9]+}", method = RequestMethod.DELETE)
    public Map<String, Object> cancelPurchaseRequest(
        @PathVariable Long market_id,
        @PathVariable Long purchase_id,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                           ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.cancelPurchaseRequest(market_id, purchase_id, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases/{purchase_id:[0-9]+}/accept", method = RequestMethod.POST)
    public Map<String, Object> acceptPurchaseRequest(@PathVariable Long market_id,
        @PathVariable Long purchase_id,
        @AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap
                .put("data", marketService.acceptPurchaseRequest(market_id, purchase_id, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases/{purchase_id:[0-9]+}/deny", method = RequestMethod.POST)
    public Map<String, Object> denyPurchaseRequest(@PathVariable Long market_id,
        @PathVariable Long purchase_id,
        @RequestParam String reason, @AuthenticationPrincipal User user, HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data",
                          marketService.denyPurchaseRequest(market_id, purchase_id, reason, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases/{purchase_id:[0-9]+}/deposit", method = RequestMethod.POST)
    public Map<String, Object> depositPurchaseRequest(
        @PathVariable Long market_id,
        @PathVariable Long purchase_id,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                                     ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap
                .put("data", marketService.depositPurchaseRequest(market_id, purchase_id, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases/{purchase_id:[0-9]+}/done", method = RequestMethod.POST)
    public Map<String, Object> donePurchaseRequest(
        @PathVariable Long market_id,
        @PathVariable Long purchase_id,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                                  ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.donePurchaseRequest(market_id, purchase_id, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/purchases/{purchase_id:[0-9]+}/report", method = RequestMethod.POST)
    public Map<String, Object> reportPurchaseRequest(
        @PathVariable Long market_id,
        @PathVariable Long purchase_id,
        @RequestParam String reason,
        @AuthenticationPrincipal User user,
        HttpServletResponse res
                                                    ) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService
                .reportPurchaseRequest(market_id, purchase_id, reason, user));
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

    @RequestMapping(value = "/{market_id:[0-9]+}/like", method = RequestMethod.POST)
    public Map<String, Object> toggleMarketLike(@PathVariable Long market_id,
        @AuthenticationPrincipal User user,
        HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.CREATED.value());
            returnMap.put("result", 1);
            returnMap.put("data", marketService.toggleMarketLike(market_id, user));
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
     * 판매 관리 및 판매 내역 API
     */
    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    public Map<String, Object> readUserSales(@AuthenticationPrincipal User user,
        @RequestParam(required = false, defaultValue = "0") Long page,
        @RequestParam(required = false, defaultValue = "RECENT|DESC") String order,
        @RequestParam(required = false) MarketStatus status,
        @RequestParam(required = false) String duration,
        @RequestParam(required = false) String query,
        HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.readUserSales(user, page, order, status, duration, query));
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
     * 판매 등록 API
     */
    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public Map<String, Object> createSale(@AuthenticationPrincipal User user,
        @RequestBody MarketSale.Request request,
        HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.CREATED.value());
            returnMap.put("result", 1);
            returnMap.put("data", marketService.createSale(user, request));
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
     * DL 구매 요청 API
     */
    @RequestMapping(value = "/purchases", method = RequestMethod.GET)
    public Map<String, Object> readUserPurchases(
        @AuthenticationPrincipal User user,
        @RequestParam(required = false, defaultValue = "0") Long page,
        @RequestParam(required = false, defaultValue = "RECENT|DESC") String order,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String duration,
        @RequestParam(required = false) String query,
        HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            res.setStatus(HttpStatus.CREATED.value());
            returnMap.put("result", 1);
            returnMap.put("data", marketService.readUserPurchases(user, page, order, status, duration,query));
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

    @RequestMapping(value = "/charge/cp", method = RequestMethod.POST)
    public Map<String, Object> chargeCP(@AuthenticationPrincipal User user,
        @RequestParam BigDecimal amount,
        HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.chargeCP(user, amount));
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
     * 마켓의 현재 상태를 확인하는 API
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Map<String, Object> readMarketInfo(HttpServletResponse res) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            returnMap.put("result", 1);
            returnMap.put("data", marketService.readMarketInfo());
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