package com.laon.cashlink.common.define.market;

import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum MarketStatus implements CodeEnum {

    INIT("INIT"),

    ON_SALE("ON_SALE"),

    DONE("DONE"),

    EXPIRED("EXPIRED"),

    CANCEL("CANCEL");

    MarketStatus(String status) {
        this.status = status;
    }

    private String status;

    @Override
    public String toString() {
        return this.status;
    }

    @MappedTypes(MarketStatus.class)
    public static class TypeHandler extends CodeEnumTypeHandler<MarketStatus> {
        public TypeHandler() {
            super(MarketStatus.class);
        }
    }

}