package com.laon.cashlink.repository.user;

import com.laon.cashlink.entity.user.Notification;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotiRepository {

    private final SqlSessionTemplate sql;

    public List<Notification> readNotiList(Map<String, Object> payload) {
        return sql.selectList("NotiRepository.readNotiList", payload);
    }

    public Long countNotiList(Map<String, Object> payload) {
        return sql.selectOne("NotiRepository.countNotiList", payload);
    }

    public Notification readNoti(Map<String, Object> payload) {
        return sql.selectOne("NotiRepository.readNoti", payload);
    }

    public Boolean checkNotReadNoti(Map<String, Object> payload) {
        return sql.selectOne("NotiRepository.checkNotReadNoti", payload);
    }

    public void updateNoti(Map<String, Object> payload) {
        sql.update("NotiRepository.updateNoti", payload);
    }

}