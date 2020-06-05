package com.laon.cashlink.common.define;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum InquiryStatus implements CodeEnum {

    @JsonProperty("NOT_ANSWER")
    NOT_ANSWER("NOT_ANSWER"),

    @JsonProperty("ANSWER")
    ANSWER("ANSWER");

    private final String status;

    InquiryStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }

    @MappedTypes(InquiryStatus.class)
    public static class TypeHandler extends CodeEnumTypeHandler<InquiryStatus> {
        public TypeHandler() {
            super(InquiryStatus.class);
        }
    }

}
