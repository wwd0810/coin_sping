package com.laon.cashlink.entity.dto.market;

import com.laon.cashlink.common.define.user.AccountType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class MarketSale {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private AccountType type;

        private BigDecimal amount;

        private BigDecimal price;

    }

}
