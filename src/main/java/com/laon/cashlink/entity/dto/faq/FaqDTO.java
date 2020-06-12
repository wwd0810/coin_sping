package com.laon.cashlink.entity.dto.faq;

import lombok.*;

import javax.validation.constraints.NotEmpty;

public class FaqDTO {


    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotEmpty
        private String question;

        @NotEmpty
        private String answer;

    }


}
