package com.laon.cashlink.service.notice;

import java.util.Map;

public interface NoticeService {

    Map<String, Object> readNoticeList(Long page, String query) throws Exception;

}
