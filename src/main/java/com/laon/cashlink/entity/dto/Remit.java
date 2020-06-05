package com.laon.cashlink.entity.dto;

import com.laon.cashlink.common.define.user.AccountType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class Remit {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String from;
        private String to;

        private AccountType type;

        private BigDecimal amount;
    }

}
