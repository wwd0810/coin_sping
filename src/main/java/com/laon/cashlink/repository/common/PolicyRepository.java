package com.laon.cashlink.repository.common;

import com.laon.cashlink.entity.common.Policy;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PolicyRepository {

    private final SqlSessionTemplate sql;

    public List<Policy> readPolicyList(Map<String, Object> payload) {
        return sql.selectList("PolicyRepository.readPolicyList", payload);
    }

    public Long countPolicyList(Map<String, Object> payload) {
        return sql.selectOne("PolicyRepository.countPolicyList", payload);
    }

    public Policy readPolicy(Map<String, Object> payload) {
        return sql.selectOne("PolicyRepository.readPolicy", payload);
    }

    public void createPolicy(Map<String, Object> payload) {
        sql.insert("PolicyRepository.createPolicy", payload);
    }

    public void updatePolicy(Map<String, Object> payload) {
        sql.update("PolicyRepository.updatePolicy", payload);
    }

}