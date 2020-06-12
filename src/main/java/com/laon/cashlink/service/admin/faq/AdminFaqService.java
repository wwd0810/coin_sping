package com.laon.cashlink.service.admin.faq;

import com.laon.cashlink.entity.dto.faq.FaqDTO;

import com.laon.cashlink.entity.user.User;

import java.util.Map;

public interface AdminFaqService {
    Map<String, Object> readFaq(User user, Long faq_id) throws Exception;

    Map<String, Object> createFaq(User user, FaqDTO.Request request) throws Exception;

    Map<String, Object> updateFaq(User user, Long faq_id, FaqDTO.Request request) throws Exception;

    Map<String, Object> deleteFaq(User user, Long faq_id) throws Exception;

}
