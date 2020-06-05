package com.laon.cashlink.entity.user;

import com.laon.cashlink.common.define.user.UserGender;
import java.time.LocalDate;
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
public class User {

        private Long id;

        private String username;

        private String name;

        private String phone;

        private LocalDate birth;

        private UserGender sex;

        private String token;

        private LocalDateTime created_at;
        private LocalDateTime updated_at;

}