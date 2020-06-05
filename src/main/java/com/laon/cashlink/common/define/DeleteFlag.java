package com.laon.cashlink.common.define;

import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum DeleteFlag implements CodeEnum {

    Y("Y"),

    N("N");

    DeleteFlag(String flag) {
        this.flag = flag;
    }

    private String flag;

    @Override
    public String toString() {
        return this.flag;
    }

    @MappedTypes(DeleteFlag.class)
    public static class TypeHandler extends CodeEnumTypeHandler<DeleteFlag> {
        public TypeHandler() {
            super(DeleteFlag.class);
        }
    }

}