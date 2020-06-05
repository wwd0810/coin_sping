package com.laon.cashlink.entity.user;

import com.laon.cashlink.common.define.user.AccountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

        private String id;

        private Long user_id;

        private AccountType type;

        @Builder.Default
        private BigDecimal quantity = BigDecimal.ZERO;

        private LocalDateTime created_at;
        private LocalDateTime updated_at;

}