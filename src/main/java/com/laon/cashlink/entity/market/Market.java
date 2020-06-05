package com.laon.cashlink.entity.market;

import com.laon.cashlink.common.define.market.MarketStatus;
import com.laon.cashlink.entity.user.UserMinify;
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
public class Market {

        private Long id;

        private BigDecimal quantity;
        private BigDecimal price;
        private BigDecimal fees;

        private MarketStatus status;

        private Boolean isLike;

        private LocalDateTime canceled_at;
        private LocalDateTime approved_at;

        private LocalDateTime created_at;
        private LocalDateTime updated_at;

        private UserMinify seller;

        private Purchase purchase;

}