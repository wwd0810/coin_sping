package com.laon.cashlink.entity.common;

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
public class Faq {

    private Long id;

    private String question;
    private String answer;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
