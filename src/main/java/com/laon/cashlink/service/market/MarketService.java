package com.laon.cashlink.service.market;

import com.laon.cashlink.common.define.market.MarketStatus;
import com.laon.cashlink.entity.dto.market.MarketSale;
import com.laon.cashlink.entity.user.User;
import java.math.BigDecimal;
import java.util.Map;

public interface MarketService {

    Map<String, Object> readMarketList(User user, Long page, Integer size, String order, String query) throws Exception;

    Map<String, Object> readMarket(Long market_id) throws Exception;

    Map<String, Object> updateMarket(Long market_id, MarketSale.Request request, User user) throws Exception;

    Map<String, Object> cancelPurchase(User user, Long market_id) throws Exception;

    Map<String, Object> readPurchaseList(Long makret_id, Long page, User user) throws Exception;

    Map<String, Object> readPurchase(Long market_id, Long purchase_id, User user) throws Exception;

    Map<String, Object> cancelPurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception;

    Map<String, Object> acceptPurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception;

    Map<String, Object> denyPurchaseRequest(Long market_id, Long purchase_id, String reason, User user)
            throws Exception;

    Map<String, Object> depositPurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception;

    Map<String, Object> donePurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception;

    Map<String, Object> reportPurchaseRequest(Long market_id, Long purchase_id, String reason, User user) throws Exception;

    Map<String, Object> readUserSales(User user, Long page, String order, MarketStatus status, String duration, String query) throws Exception;

    Map<String, Object> createSale(User user, MarketSale.Request request) throws Exception;

    Map<String, Object> createPurchaseRequest(User User, Long market_id) throws Exception;

    Map<String, Object> readUserPurchases(User user, Long page, String order, String status, String duration, String query) throws Exception;

    Map<String, Object> toggleMarketLike(Long market_id, User user) throws Exception;

    Map<String, Object> chargeCP(User user, BigDecimal amount) throws Exception;

    Map<String, Object> readMarketInfo() throws Exception;

}