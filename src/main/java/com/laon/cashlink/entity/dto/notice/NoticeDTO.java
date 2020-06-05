package com.laon.cashlink.entity.dto.notice;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class NoticeDTO {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotEmpty
        private String title;

        @NotEmpty
        private String contents;

    }

}
