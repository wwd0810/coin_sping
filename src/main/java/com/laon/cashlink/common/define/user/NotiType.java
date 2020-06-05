package com.laon.cashlink.common.define.user;

import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum NotiType implements CodeEnum {

    TRADE("TRADE"),

    WALLET("WALLET"),

    GIFT("GIFT"),

    ETC("ETC");
    
    NotiType(String type) {
        this.type = type;
    }
    
    private String type;

    @Override
    public String toString() {
        return this.type;
    }

    @MappedTypes(NotiType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<NotiType> {
        public TypeHandler() {
            super(NotiType.class);
        }
    }

}