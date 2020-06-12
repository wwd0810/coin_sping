package com.laon.cashlink.service.admin.notice;

import com.laon.cashlink.entity.dto.notice.NoticeDTO;
import com.laon.cashlink.entity.user.User;
import java.util.Map;

public interface AdminNoticeService {

    Map<String, Object> readNotice(User user, Long notice_id) throws Exception;

    Map<String, Object> createNotice(NoticeDTO.Request request, User user) throws Exception;

    Map<String, Object> updateNotice(Long notice_id, NoticeDTO.Request request, User user) throws Exception;

    Map<String, Object> deleteNotice(Long notice_id) throws Exception;

}
