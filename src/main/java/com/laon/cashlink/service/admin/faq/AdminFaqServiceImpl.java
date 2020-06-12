package com.laon.cashlink.service.admin.faq;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.Faq;
import com.laon.cashlink.entity.dto.faq.FaqDTO;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Service("AdminFaqService")
@RequiredArgsConstructor
public class AdminFaqServiceImpl implements AdminFaqService {

    private final FaqRepository faqRepository;

    @Override
    public Map<String, Object> readFaq(User user, Long faq_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("faq_id", faq_id);
        }

        Faq res = faqRepository.readFaq(payload);

        if (ObjectUtils.isEmpty(res))
            throw new ApiException(ApiErrorCode.FAQ_NOT_FOUND);

        {
            returnMap.put("list", res);
        }

        return returnMap;
    }

    @Override
    public Map<String, Object> createFaq(User user, FaqDTO.Request request) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("question", request.getQuestion());
            payload.put("answer", request.getAnswer());
        }

        faqRepository.createFaq(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> updateFaq(User user, Long faq_id, FaqDTO.Request request) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("faq_id", faq_id);
        }

        Faq faq = faqRepository.readFaq(payload);

        if(ObjectUtils.isEmpty(faq)) throw new ApiException(ApiErrorCode.FAQ_NOT_FOUND);

        {
            payload.put("faq_id", faq_id);
            payload.put("question", request.getQuestion());
            payload.put("answer", request.getAnswer());
        }

        faqRepository.updateFaq(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> deleteFaq(User user, Long faq_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("faq_id", faq_id);
        }

        faqRepository.deleteFaq(payload);

        return returnMap;
    }
}
