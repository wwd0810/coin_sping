package com.laon.cashlink.entity.user;

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
public class UserMinify {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}