package com.laon.cashlink.service.faq;

import com.laon.cashlink.entity.user.User;

import java.util.Map;

public interface FaqService {

    Map<String, Object> readFaqList(String query) throws Exception;



}
