package com.laon.cashlink.repository.common;

import com.laon.cashlink.entity.common.Inquiry;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InquiryRepository {

    private final SqlSessionTemplate sql;

    public List<Inquiry> readAdminInquiryList(Map<String, Object> payload) {
        return sql.selectList("InquiryRepository.readAdminInquiryList", payload);
    }

    public List<Inquiry> readInquiryList(Map<String, Object> payload) {
        return sql.selectList("InquiryRepository.readInquiryList", payload);
    }

    public Inquiry readInquiry(Map<String, Object> payload) {
        return sql.selectOne("InquiryRepository.readInquiry", payload);
    }

    public Long countInquiry(Map<String, Object> payload) {
        return sql.selectOne("InquiryRepository.countInquiry", payload);
    }

    public void createInquiry(Map<String, Object> payload) {
        sql.insert("InquiryRepository.createInquiry", payload);
    }

    public void updateInquiry(Map<String, Object> payload) {
        sql.update("InquiryRepository.updateInquiry", payload);
    }

    public void deleteInquiry(Map<String, Object> payload) {
        sql.delete("InquiryRepository.deleteInquiry", payload);
    }

}
