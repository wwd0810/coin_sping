package com.laon.cashlink.entity.market;

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
public class Report {

    private UserMinify from;
    private UserMinify to;

    private String reason;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
