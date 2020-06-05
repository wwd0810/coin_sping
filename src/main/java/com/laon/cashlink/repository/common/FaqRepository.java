package com.laon.cashlink.repository.common;

import com.laon.cashlink.entity.common.Faq;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FaqRepository {

    private final SqlSessionTemplate sql;

    public List<Faq> readFaqList(Map<String, Object> payload) {
        return sql.selectList("FaqRepository.readFaqList", payload);
    }

}
