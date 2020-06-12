package com.laon.cashlink.entity.market;

import com.laon.cashlink.common.define.market.PurchaseStatus;
import com.laon.cashlink.entity.user.UserMinify;
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
public class Purchase {

        private Long id;

        private PurchaseStatus status;

        private String reason;

        private LocalDateTime canceled_at;
        private LocalDateTime approved_at;

        private LocalDateTime created_at;
        private LocalDateTime updated_at;

        private MarketMinified market;

        private UserMinify buyer;

        private UserMinify seller;

}