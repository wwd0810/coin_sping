package com.laon.cashlink.entity.user;

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
public class Transaction {

    private Long id;

    private String title;
    private String description;

    private BigDecimal amount;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private Account from;
    private Account to;

    private UserMinify from_user;
    private UserMinify to_user;

}
