package com.laon.cashlink.repository.common;

import com.laon.cashlink.entity.common.Inquiry;
import com.laon.cashlink.entity.common.ServiceInfo;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ServiceInfoRepository {

    private final SqlSessionTemplate sql;

    public ServiceInfo readServiceInfo(Map<String, Object> payload) {
        return sql.selectOne("ServiceInfoRepository.readServiceInfo", payload);
    }

    public void updateServiceInfo(Map<String, Object> payload) {
        sql.update("ServiceInfoRepository.updateServiceInfo", payload);
    }

}
