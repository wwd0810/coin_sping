package com.laon.cashlink.service.admin.inquiry;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.Inquiry;
import com.laon.cashlink.entity.common.Order;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("AdminInquiryService")
@RequiredArgsConstructor
public class AdminInquiryServiceImpl implements AdminInquiryService {

    private final InquiryRepository inquiryRepository;

    @Override
    public Map<String, Object> readAdminInquiry(String query ,Long page, Integer size, String orderType, String status) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        Order order = Order.parse(orderType);

        Paging paging = Paging.builder().page(page).build();

        {
            payload.put("query" , query);
            payload.put("paging", paging);
            payload.put("order", order);
            payload.put("status", status);
        }

        List<Inquiry> list = inquiryRepository.readAdminInquiryList(payload);

        Long count = inquiryRepository.countInquiry(payload);
        paging.setCount(count);

        {
            returnMap.put("list", list);
            returnMap.put("paging", paging);
        }

        return returnMap;

    }

    @Override
    public Map<String, Object> readInquiry(User user, Long inquiry_id) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("inquiry_id", inquiry_id);
        }

        Inquiry inquiry = inquiryRepository.readInquiry(payload);

        if (ObjectUtils.isEmpty(inquiry))
            throw new ApiException(ApiErrorCode.INQUIRY_NOT_FOUND);

        {
            returnMap.put("data", inquiry);
        }

        return returnMap;

    }

    @Override
    public Map<String, Object> updateInquiry(User user, Long inquiry_id, String answer) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();


        {
            payload.put("inquiry_id", inquiry_id);
        }

        Inquiry inquiry = inquiryRepository.readInquiry(payload);

        if (ObjectUtils.isEmpty(inquiry)) throw new ApiException(ApiErrorCode.INQUIRY_NOT_FOUND);

        {
            payload.clear();
            payload.put("inquiry_id", inquiry_id);
            payload.put("answer", answer);
            payload.put("status", "ANSWER");
        }

        inquiryRepository.updateInquiry(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> deleteInqiury(User user, Long inquiry_id) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("inquiry_id", inquiry_id);
        }

        Inquiry inquiry = inquiryRepository.readInquiry(payload);

        if (ObjectUtils.isEmpty(inquiry)) throw new ApiException(ApiErrorCode.INQUIRY_NOT_FOUND);


        {
            payload.clear();
            payload.put("inquiry_id", inquiry_id);
        }

        inquiryRepository.deleteInquiry(payload);

        return returnMap;

    }
}
