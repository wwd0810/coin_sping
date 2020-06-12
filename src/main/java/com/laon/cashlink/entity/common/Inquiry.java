package com.laon.cashlink.entity.common;

import com.laon.cashlink.common.define.InquiryStatus;
import java.time.LocalDateTime;

import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.entity.user.UserMinify;
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
public class Inquiry {

    private Long id;

    private String title;
    private String contents;

    private String answer;

    private InquiryStatus status;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private UserMinify user;

}
