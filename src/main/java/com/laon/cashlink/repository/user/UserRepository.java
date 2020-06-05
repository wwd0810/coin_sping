package com.laon.cashlink.repository.user;

import com.laon.cashlink.entity.user.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SqlSessionTemplate sql;

    public User readUser(Map<String, Object> payload) {
        return sql.selectOne("UserRepository.readUser", payload);
    }

    public void createUser(Map<String, Object> payload) {
        sql.insert("UserRepository.createUser", payload);
    }

    public void updateUser(Map<String, Object> payload) {
        sql.update("UserRepository.updateUser", payload);
    }

}