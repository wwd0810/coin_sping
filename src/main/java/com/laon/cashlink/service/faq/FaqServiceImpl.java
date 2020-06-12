package com.laon.cashlink.service.faq;

import com.laon.cashlink.entity.common.Faq;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.FaqRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.util.internal.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service("FaqService")
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public Map<String, Object> readFaqList(String query) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("query", query);
        }

        List<Faq> faqList = faqRepository.readFaqList(payload);

        {
            returnMap.put("list", faqList);
        }
        return returnMap;
    }


}
