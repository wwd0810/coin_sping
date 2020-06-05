package com.laon.cashlink.repository.market;

import com.laon.cashlink.entity.market.Purchase;
import com.laon.cashlink.entity.market.Report;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PurchaseRepository {

    private final SqlSessionTemplate sql;

    public Purchase readPurchase(Map<String, Object> payload) {
        return sql.selectOne("PurchaseRepository.readPurchase", payload);
    }

    public List<Purchase> readPurchaseList(Map<String, Object> payload) {
        return sql.selectList("PurchaseRepository.readPurchaseList", payload);
    }

    public Long countPurchaseList(Map<String, Object> payload) {
        return sql.selectOne("PurchaseRepository.countPurchaseList", payload);
    }

    public Report readPurchaseReport(Map<String, Object> payload) {
        return sql.selectOne("PurchaseRepository.readPurchaseReport", payload);
    }

    public void createPurchase(Map<String, Object> payload) {
        sql.insert("PurchaseRepository.createPurchase", payload);
    }

    public void createPurchaseReport(Map<String, Object> payload) {
        sql.insert("PurchaseRepository.createPurchaseReport", payload);
    }

    public void updatePurchase(Map<String, Object> payload) {
        sql.update("PurchaseRepository.updatePurchase", payload);
    }

}