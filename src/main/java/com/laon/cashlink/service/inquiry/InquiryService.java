package com.laon.cashlink.service.inquiry;

import com.laon.cashlink.entity.dto.common.ApplyInquiry;
import com.laon.cashlink.entity.user.User;
import java.util.Map;

public interface InquiryService {

    Map<String, Object> readInquiryList(User user) throws Exception;

    Map<String, Object> createInquiry(User user, ApplyInquiry.Request request) throws Exception;

}
