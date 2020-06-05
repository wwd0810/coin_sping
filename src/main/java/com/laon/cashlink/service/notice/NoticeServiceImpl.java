package com.laon.cashlink.service.notice;

import com.laon.cashlink.entity.common.Notice;
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
    public Map<String, Object> readNoticeList(Long page) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        List<Notice> noticeList = noticeRepository.readNoticeList(payload);

        {
            returnMap.put("list", noticeList);
        }
        return returnMap;
    }

}
