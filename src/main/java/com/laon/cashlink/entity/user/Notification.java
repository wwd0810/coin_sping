package com.laon.cashlink.entity.user;

import com.laon.cashlink.common.define.DeleteFlag;
import com.laon.cashlink.common.define.user.NotiStatus;
import com.laon.cashlink.common.define.user.NotiSubType;
import com.laon.cashlink.common.define.user.NotiType;
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
public class Notification {

        private Long id;

        private String prefix;
        private String message;
        private String suffix;

        private NotiType type;

        private NotiSubType sub_type;

        private NotiStatus status;

        @Builder.Default
        private DeleteFlag delete_yn = DeleteFlag.N;

        private Long user_id;

}