package com.laon.cashlink.service.admin.market;

import java.util.Map;

public interface AdminMarketService {

    Map<String, Object> readDealList(String query, Long page, String duration, Integer quanLow, Integer quanHigh, Integer priceLow, Integer priceHigh) throws Exception;

    Map<String, Object> readProductList(String query, Long page, String duration, Integer quanLow, Integer quanHigh, Integer priceLow, Integer priceHigh) throws Exception;

    Map<String, Object> countDeals() throws Exception;

}
