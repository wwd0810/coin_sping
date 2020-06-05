package com.laon.cashlink.entity.dto.policy;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

public class PolicyDTO {

    @Data
    @Builder
    @ToString
    @NotEmpty
    @AllArgsConstructor
    public static class Request {

        @NotEmpty
        private String key;

        @NotEmpty
        private String value;

        private String description;

    }

}
