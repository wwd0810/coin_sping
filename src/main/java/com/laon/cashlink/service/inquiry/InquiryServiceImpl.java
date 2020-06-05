package com.laon.cashlink.service.inquiry;

import com.laon.cashlink.entity.common.Inquiry;
import com.laon.cashlink.entity.dto.common.ApplyInquiry;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.InquiryRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("InquiryService")
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;

    @Override
    public Map<String, Object> readInquiryList(User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("user_id", user.getId());
        }
        List<Inquiry> inquiryList = inquiryRepository.readInquiryList(payload);

        {
            returnMap.put("list", inquiryList);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> createInquiry(User user, ApplyInquiry.Request request) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("user_id", user.getId());
            payload.put("title", request.getTitle());
            payload.put("contents", request.getContents());
        }

        inquiryRepository.createInquiry(payload);

        return returnMap;
    }

}
