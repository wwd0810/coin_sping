package com.laon.cashlink.repository.market;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MarketLikeRepository {

    private final SqlSessionTemplate sql;

    public Long countLike(Map<String, Object> payload) {
        return sql.selectOne("MarketLikeRepository.countLike", payload);
    }

    public Boolean isLike(Map<String, Object> payload) {
        return sql.selectOne("MarketLikeRepository.isLike", payload);
    }

    public void like(Map<String, Object> payload) {
        sql.insert("MarketLikeRepository.like", payload);
    }

    public void unLike(Map<String, Object> payload) {
        sql.delete("MarketLikeRepository.unLike", payload);
    }

}
