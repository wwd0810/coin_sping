package com.laon.cashlink.common.define.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum AccountType implements CodeEnum {

    @JsonProperty("COIN_POINT")
    CP("COIN_POINT"),

    @JsonProperty("DILLING")
    DL("DILLING"),

    @JsonProperty("DILLING_COIN")
    DLC("DILLING_COIN");

    AccountType(String code) {
        this.code = code;
    }

    private final String code;

    @Override
    public String toString() {
        return this.code;
    }

    @MappedTypes(AccountType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<AccountType> {
        public TypeHandler() {
            super(AccountType.class);
        }
    }

}
