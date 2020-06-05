package com.laon.cashlink.service.faq;

import com.laon.cashlink.entity.common.Faq;
import com.laon.cashlink.repository.common.FaqRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("FaqService")
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public Map<String, Object> readFaqList() throws Exception {
        Map<String, Object> returnMap = new HashMap<>();

        List<Faq> faqList = faqRepository.readFaqList(null);

        {
            returnMap.put("list", faqList);
        }
        return returnMap;
    }

}
