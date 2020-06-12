package com.laon.cashlink.service.admin.notice;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.Notice;
import com.laon.cashlink.entity.dto.notice.NoticeDTO;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.NoticeRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service("AdminNoticeService")
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Map<String, Object> readNotice(User user, Long notice_id) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("notice_id", notice_id);
        }

        Notice notice = noticeRepository.readNotice(payload);

        if (ObjectUtils.isEmpty(notice))
            throw new ApiException(ApiErrorCode.NOTICE_NOT_FOUND);

        {
            returnMap.put("data", notice);
        }

        return returnMap;

    }

    @Override
    public Map<String, Object> createNotice(NoticeDTO.Request request, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("title", request.getTitle());
            payload.put("contents", request.getContents());
            payload.put("created_by", user.getId());
        }
        noticeRepository.createNotice(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> updateNotice(Long notice_id, NoticeDTO.Request request, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("notice_id", notice_id);
        }
        Notice notice = noticeRepository.readNotice(payload);
        if (ObjectUtils.isEmpty(notice)) throw new ApiException(ApiErrorCode.NOTICE_NOT_FOUND);

        {
            payload.clear();
            payload.put("notice_id", notice_id);
            payload.put("title", request.getTitle());
            payload.put("contents", request.getContents());
        }
        noticeRepository.updateNotice(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> deleteNotice(Long notice_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("notice_id", notice_id);
        }
        Notice notice = noticeRepository.readNotice(payload);
        if (ObjectUtils.isEmpty(notice)) throw new ApiException(ApiErrorCode.NOTICE_NOT_FOUND);

        {
            payload.clear();
            payload.put("notice_id", notice.getId());
        }
        noticeRepository.deleteNotice(payload);

        return returnMap;
    }

}
