package com.laon.cashlink.repository.user;

import com.laon.cashlink.entity.user.Account;
import com.laon.cashlink.entity.user.Transaction;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final SqlSessionTemplate sql;

    public Account readAccount(Map<String, Object> payload) {
        return sql.selectOne("AccountRepository.readAccount", payload);
    }

    public List<Account> readAccountList(Map<String, Object> payload) {
        return sql.selectList("AccountRepository.readAccountList", payload);
    }

    public List<Transaction> readTransactionList(Map<String, Object> payload) {
        return sql.selectList("AccountRepository.readTransactionList", payload);
    }

    public Long countTransactionList(Map<String, Object> payload) {
        return sql.selectOne("AccountRepository.countTransactionList", payload);
    }

    public void createAccount(Map<String, Object> payload) {
        sql.insert("AccountRepository.createAccount", payload);
    }

    public void createTransaction(Map<String, Object> payload) {
        sql.insert("AccountRepository.createTransaction", payload);
    }
    
    public void updateAccount(Map<String, Object> payload) {
        sql.update("AccountRepository.updateAccount", payload);
    }

}