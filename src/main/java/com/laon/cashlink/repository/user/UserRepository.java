package com.laon.cashlink.repository.user;

import com.laon.cashlink.entity.user.User;

import java.util.List;
import java.util.Map;

import com.laon.cashlink.entity.user.UserList;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final SqlSessionTemplate sql;

    public List<User> readUserList(Map<String, Object> payload) {
        return sql.selectList("UserRepository.readUserList", payload);
    }

    public Long countUserList(Map<String, Object> payload) {
        return sql.selectOne("UserRepository.countUserList", payload);
    }

    // PIN
    public Boolean duplicatePinPass(Map<String, Object> payload) {
        return sql.selectOne("UserRepository.duplicatePinPass", payload);
    }

    public void updatePinPass (Map<String, Object> payload) {
        sql.update("UserRepository.updatePinPass", payload);
    }

    //
    public Boolean checkUserPin(Map<String, Object> payload) {
        return  sql.selectOne("UserRepository.checkUserPin", payload);
    }

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