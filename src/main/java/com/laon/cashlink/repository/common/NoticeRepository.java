package com.laon.cashlink.repository.common;

import com.laon.cashlink.entity.common.Notice;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    private final SqlSessionTemplate sql;

    public List<Notice> readNoticeList(Map<String, Object> payload) {
        return sql.selectList("NoticeRepository.readNoticeList", payload);
    }

    public Long countNoticeList(Map<String, Object> payload) {
        return sql.selectOne("NoticeRepository.countNoticeList", payload);
    }

    public Notice readNotice(Map<String, Object> payload) {
        return sql.selectOne("NoticeRepository.readNotice", payload);
    }

    public void createNotice(Map<String, Object> payload) {
        sql.insert("NoticeRepository.createNotice", payload);
    }

    public void updateNotice(Map<String, Object> payload) {
        sql.update("NoticeRepository.updateNotice", payload);
    }

    public void deleteNotice(Map<String, Object> payload) {
        sql.delete("NoticeRepository.deleteNotice", payload);
    }

}
