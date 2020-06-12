package com.laon.cashlink.service.admin.inquiry;

import com.laon.cashlink.entity.user.User;

import java.util.Map;

public interface AdminInquiryService {

    Map<String, Object> readAdminInquiry(String query,Long page, Integer size, String orderType, String status) throws Exception;

    Map<String, Object> readInquiry(User user, Long inquiry_id) throws Exception;

    Map<String, Object> updateInquiry(User user, Long inquiry_id, String answer) throws Exception;

    Map<String, Object> deleteInqiury(User user, Long inquiry_id) throws Exception;

}
