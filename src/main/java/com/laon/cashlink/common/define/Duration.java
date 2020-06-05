package com.laon.cashlink.common.define;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum Duration implements CodeEnum {

    @JsonProperty("1WEEK")
    ONE_WEEK("1WEEK"),

    @JsonProperty("1MONTH")
    ONE_MONTH("1MONTH"),

    @JsonProperty("3MONTH")
    THREE_MONTH("3MONTH"),

    @JsonProperty("6MONTH")
    SIX_MONTH("6MONTH"),

    @JsonProperty("1YEAR")
    ONE_YEAR("1YEAR");

    private final String code;

    Duration(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }

    @MappedTypes(Duration.class)
    public static class TypeHandler extends CodeEnumTypeHandler<Duration> {
        public TypeHandler() {
            super(Duration.class);
        }
    }

}
