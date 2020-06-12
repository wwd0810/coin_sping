package com.laon.cashlink.service.admin.market;

import com.laon.cashlink.entity.common.MarketInfo;
import com.laon.cashlink.entity.common.Order;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.entity.market.Market;
import com.laon.cashlink.entity.market.Purchase;
import com.laon.cashlink.repository.market.MarketRepository;
import com.laon.cashlink.repository.market.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("AdminMarketService")
@RequiredArgsConstructor
public class AdminMarketServiceImpl implements AdminMarketService {

    private final PurchaseRepository purchaseRepository;
    private final MarketRepository marketRepository;


    @Override
    public Map<String, Object> countDeals() throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        List<MarketInfo> data = marketRepository.countDeals(payload);

        {
            returnMap.put("count", data);

        }

        return returnMap;

    }

    @Override
    public Map<String, Object> readProductList(String query, Long page, String duration, Integer quanLow, Integer quanHigh, Integer priceLow, Integer priceHigh) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        Paging paging = Paging.builder().page(page).build();
        Order order = Order.parse("RECENT|DESC");

        {
            payload.put("paging", paging);
            payload.put("query", query);
            payload.put("order",order);
            payload.put("duration", duration);
            payload.put("quanLow", quanLow);
            payload.put("quanHigh", quanHigh);
            payload.put("priceLow", priceLow);
            payload.put("priceHigh", priceHigh);
        }

        List<Market> markets = marketRepository.readMarketList(payload);
        Long count = marketRepository.countMarketList(payload);
        paging.setCount(count);

        {
            returnMap.put("paging", paging);
            returnMap.put("list" , markets);
        }

        return returnMap;

    }

    /*
    *  삭제예정
    * */
    @Override
    public Map<String, Object> readDealList(String query, Long page, String duration, Integer quanLow, Integer quanHigh, Integer priceLow, Integer priceHigh) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        Paging paging = Paging.builder().page(page).build();

        {
            payload.put("paging", paging);
            payload.put("query", query);
            payload.put("duration", duration);
            payload.put("quanLow", quanLow);
            payload.put("quanHigh", quanHigh);
            payload.put("priceLow", priceLow);
            payload.put("priceHigh", priceHigh);
        }

        List<Purchase> purchases = purchaseRepository.readDealList(payload);
        Long count = purchaseRepository.countPurchaseList(payload);
        paging.setCount(count);

        {
            returnMap.put("paging", paging);
            returnMap.put("list" , purchases);
        }

        return returnMap;

    }
}
