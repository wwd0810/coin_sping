package com.laon.cashlink.repository.market;

import com.laon.cashlink.entity.common.MarketInfo;
import com.laon.cashlink.entity.market.Market;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MarketRepository {

    private final SqlSessionTemplate sql;

    //SELECT
    public List<Market> readMarketList(Map<String, Object> payload) {
        return sql.selectList("MarketRepository.readMarketList", payload);
    }

    public Long countMarketList(Map<String, Object> payload) {
        return sql.selectOne("MarketRepository.countMarketList", payload);
    }

    public Market readMarket(Map<String, Object> payload) {
        return sql.selectOne("MarketRepository.readMarket", payload);
    }

    public List<MarketInfo> countDeals (Map<String, Object> payload) {
        return sql.selectList("MarketRepository.countDeals", payload);
    }

    //CREATE
    public void createMarket(Map<String, Object> payload) {
        sql.insert("MarketRepository.createMarket", payload);
    }

    //UPDATE
    public void updateMarket(Map<String, Object> payload) {
        sql.update("MarketRepository.updateMarket", payload);
    }

}