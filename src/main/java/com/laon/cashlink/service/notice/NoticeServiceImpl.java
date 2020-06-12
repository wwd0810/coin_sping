package com.laon.cashlink.service.notice;

import com.laon.cashlink.entity.common.Notice;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.repository.common.NoticeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("NoticeService")
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Map<String, Object> readNoticeList(Long page, String query) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        Paging paging = Paging.builder().page(page).build();
        {

            payload.put("paging", paging);
            payload.put("query", query);
        }

        List<Notice> noticeList = noticeRepository.readNoticeList(payload);
        Long count = noticeRepository.countNoticeList(payload);
        paging.setCount(count);
        {
            returnMap.put("paging", paging);
            returnMap.put("list", noticeList);
        }

        return returnMap;
    }

}
